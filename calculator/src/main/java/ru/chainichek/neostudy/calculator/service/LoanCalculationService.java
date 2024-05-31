package ru.chainichek.neostudy.calculator.service;

import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.chainichek.neostudy.calculator.dto.score.PaymentScheduleElementDto;
import ru.chainichek.neostudy.calculator.dto.score.ScoringDataDto;
import ru.chainichek.neostudy.calculator.exception.ForbiddenException;
import ru.chainichek.neostudy.calculator.logic.calculation.AmountCalculator;
import ru.chainichek.neostudy.calculator.logic.calculation.CheckCalculator;
import ru.chainichek.neostudy.calculator.logic.calculation.MonthlyPaymentCalculator;
import ru.chainichek.neostudy.calculator.logic.calculation.PaymentScheduleCalculator;
import ru.chainichek.neostudy.calculator.logic.calculation.PreScoreRateCalculator;
import ru.chainichek.neostudy.calculator.logic.calculation.PskCalculator;
import ru.chainichek.neostudy.calculator.logic.calculation.ScoreRateCalculator;
import ru.chainichek.neostudy.calculator.model.EmploymentStatus;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Service
public class LoanCalculationService implements AmountCalculator,
        CheckCalculator,
        MonthlyPaymentCalculator,
        PaymentScheduleCalculator,
        PreScoreRateCalculator,
        PskCalculator,
        ScoreRateCalculator {
    private final MathContext resultMathContext;
    private final MathContext calculationMathContext;

    private final BigDecimal insuranceRate = BigDecimal.valueOf(3);
    private final BigDecimal salaryRate = BigDecimal.valueOf(1);

    private final BigDecimal baseRate;

    public LoanCalculationService(final @Qualifier("resultMathContext") MathContext resultMathContext,
                                  final @Qualifier("calculationMathContext") MathContext calculationMathContext,
                                  final @Value("${app.property.base-rate}") BigDecimal baseRate) {
        this.resultMathContext = resultMathContext;
        this.calculationMathContext = calculationMathContext;
        this.baseRate = baseRate;
    }

    @Override
    public void check(final @NotNull ScoringDataDto scoringData) {
        if (scoringData.employment().employmentStatus() == EmploymentStatus.UNEMPLOYED) {
            throw new ForbiddenException("Cannot offer a loan for unemployed");
        }

        if (scoringData.employment().salary()
                    .multiply(BigDecimal.valueOf(25), calculationMathContext)
                    .compareTo(scoringData.amount()) < 0) {
            throw new ForbiddenException("Cannot offer a loan whose amount exceeds 25 salaries");
        }

        final int age = Period.between(scoringData.birthdate(), LocalDate.now()).getYears();
        if (age > 65 || age < 20) {
            throw new ForbiddenException("Cannot offer a loan to those under 20 or over 65");
        }

        if (scoringData.employment().workExperienceTotal() < 18 || scoringData.employment().workExperienceCurrent() < 3) {
            throw new ForbiddenException("Cannot offer a loan to those whose total experience is less 18 months or whose current experience is less 3 months");
        }
    }

    @Override
    public BigDecimal calculateAmount(final @NotNull BigDecimal amount,
                                      final boolean isInsuranceEnabled,
                                      final boolean isSalaryClient) {
        return amount
                .add(isInsuranceEnabled ? amount.multiply(BigDecimal.valueOf(6).divide(BigDecimal.valueOf(100), calculationMathContext),
                        calculationMathContext) : BigDecimal.ZERO, calculationMathContext)
                .add(isSalaryClient ? amount.multiply(BigDecimal.valueOf(0).divide(BigDecimal.valueOf(100), calculationMathContext),
                        calculationMathContext) : BigDecimal.ZERO, resultMathContext);
    }

    /**
     * <h2>Calculate monthly annuity payment</h2>
     * <p>Uses this <a href="https://www.raiffeisen.ru/wiki/kak-rasschitat-annuitetnyj-platezh/">formula</a> (date of access 05-24-2024)</p>
     *
     * @param amount the loan amount to be divided into monthly payments
     * @param rate   key loan rate
     * @param term   loan term in months
     * @return the amount of the monthly annuity payment
     */
    @Override
    public BigDecimal calculateMonthlyPayment(final @NotNull BigDecimal amount,
                                              final @NotNull BigDecimal rate,
                                              final int term) {
        final BigDecimal monthlyRate = rate.divide(BigDecimal.valueOf(100), calculationMathContext)
                .divide(BigDecimal.valueOf(12), calculationMathContext);
        return amount.multiply(
                monthlyRate.divide(
                        BigDecimal.ONE.subtract(
                                BigDecimal.ONE.add(monthlyRate, calculationMathContext)
                                        .pow(-term, calculationMathContext)
                        ), calculationMathContext
                ), resultMathContext
        );
    }

    @Override
    public List<PaymentScheduleElementDto> calculatePaymentSchedule(final @NotNull BigDecimal amount,
                                                                    final @NotNull BigDecimal rate,
                                                                    final @NotNull BigDecimal monthlyPayment,
                                                                    final int term,
                                                                    final @NotNull LocalDate loanStartDate) {
        final PaymentScheduleElementDto[] paymentScheduleElements = new PaymentScheduleElementDto[term];
        final BigDecimal monthlyRate = rate.divide(BigDecimal.valueOf(100), calculationMathContext)
                .divide(BigDecimal.valueOf(12), calculationMathContext);

        BigDecimal currentAmount = amount;
        BigDecimal totalPayment = BigDecimal.ZERO;
        LocalDate currentDate = loanStartDate;

        for (int i = 0; i < term; i++) {
            final PaymentScheduleElementDto paymentScheduleElement = calculatePaymentScheduleElement(
                    i + 1,
                    currentDate,
                    currentAmount,
                    monthlyRate,
                    monthlyPayment,
                    totalPayment
            );

            currentAmount = paymentScheduleElement.remainingDebt();
            currentDate = currentDate.plusMonths(1);
            totalPayment = paymentScheduleElement.totalPayment();

            paymentScheduleElements[i] = paymentScheduleElement;
        }

        return List.of(paymentScheduleElements);
    }

    private PaymentScheduleElementDto calculatePaymentScheduleElement(final int number,
                                                                      final @NotNull LocalDate date,
                                                                      final @NotNull BigDecimal amount,
                                                                      final @NotNull BigDecimal monthlyRate,
                                                                      final @NotNull BigDecimal monthlyPayment,
                                                                      final @NotNull BigDecimal totalPayment) {
        final BigDecimal currentTotalPayment = totalPayment.add(monthlyPayment, resultMathContext);
        final BigDecimal interestPayment = amount.multiply(monthlyRate, resultMathContext);
        final BigDecimal remainingDebt = amount.add(interestPayment, calculationMathContext)
                .subtract(monthlyPayment, resultMathContext);
        final BigDecimal debtPayment = monthlyPayment.subtract(interestPayment, resultMathContext);

        return new PaymentScheduleElementDto(number,
                date,
                currentTotalPayment,
                interestPayment,
                debtPayment,
                remainingDebt
                );
    }

    @Override
    public BigDecimal calculatePreScoreRate(final boolean isInsuranceEnabled,
                                            final boolean isSalaryClient) {
        return baseRate
                .subtract(isInsuranceEnabled ? insuranceRate : BigDecimal.ZERO, calculationMathContext)
                .subtract(isSalaryClient ? salaryRate : BigDecimal.ZERO, resultMathContext);
    }

    @Override
    public BigDecimal calculatePsk(final @NotNull BigDecimal monthPayment,
                                   final @NotNull Integer term) {
        return monthPayment.multiply(BigDecimal.valueOf(term), resultMathContext);
    }


    @Override
    public BigDecimal calculateScoreRate(final @NotNull ScoringDataDto scoringData) {
        BigDecimal rate = baseRate;

        switch (scoringData.employment().employmentStatus()) {
            case SELF_EMPLOYED -> rate = rate.add(BigDecimal.valueOf(2), calculationMathContext);
            case BUSINESS_OWNER -> rate = rate.add(BigDecimal.valueOf(3), calculationMathContext);
        }


        switch (scoringData.employment().position()) {
            case MIDDLE_MANAGER -> rate = rate.subtract(BigDecimal.valueOf(2), calculationMathContext);
            case TOP_MANAGER -> rate = rate.subtract(BigDecimal.valueOf(3), calculationMathContext);
        }


        switch (scoringData.maritalStatus()) {
            case MARRIED -> rate = rate.subtract(BigDecimal.valueOf(3), calculationMathContext);
            case DIVORCED -> rate = rate.add(BigDecimal.ONE, calculationMathContext);
        }

        final int age = Period.between(scoringData.birthdate(), LocalDate.now()).getYears();
        switch (scoringData.gender()) {
            case MALE -> {
                if (age >= 30 && age < 55) {
                    rate = rate.subtract(BigDecimal.valueOf(3), calculationMathContext);
                }
            }
            case FEMALE -> {
                if (age >= 32 && age < 60) {
                    rate = rate.subtract(BigDecimal.valueOf(3), calculationMathContext);
                }
            }
        }

        rate = rate.subtract(scoringData.isInsuranceEnabled() ? insuranceRate :BigDecimal.ZERO, calculationMathContext)
                .subtract(scoringData.isSalaryClient() ? salaryRate : BigDecimal.ZERO, resultMathContext);

        return rate;
    }
}
