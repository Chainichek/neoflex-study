package ru.chainichek.neostudy.calculator.service;

import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.Arrays;
import java.util.List;

@Service
public class LoanCalculationService implements AmountCalculator,
        CheckCalculator,
        MonthlyPaymentCalculator,
        PaymentScheduleCalculator,
        PreScoreRateCalculator,
        PskCalculator,
        ScoreRateCalculator {
    private final static Logger LOG = LoggerFactory.getLogger(LoanCalculationService.class);

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
        LOG.debug("Starting to check scoring data");

        if (scoringData.employment().employmentStatus() == EmploymentStatus.UNEMPLOYED) {
            LOG.debug("Can't check further and throwing exception because scoringData.employment.employmentStatus is UNEMPLOYED");
            throw new ForbiddenException("Cannot offer a loan for unemployed");
        }

        if (scoringData.employment().salary()
                    .multiply(BigDecimal.valueOf(25), calculationMathContext)
                    .compareTo(scoringData.amount()) < 0) {
            LOG.debug("Can't check further and throwing exception because scoringData.employment.salary * 25 = %s is more than scoringData.amount = %s"
                    .formatted(scoringData.employment().salary()
                            .multiply(BigDecimal.valueOf(25), calculationMathContext), scoringData.amount()));
            throw new ForbiddenException("Cannot offer a loan whose amount exceeds 25 salaries");
        }

        final int age = Period.between(scoringData.birthdate(), LocalDate.now()).getYears();
        if (age > 65 || age < 20) {
            LOG.debug("Can't check further and throwing exception because period between scoringData.birthdate = %s and now is under 20 or over 65"
                    .formatted(Period.between(scoringData.birthdate(), LocalDate.now()).getYears()));
            throw new ForbiddenException("Cannot offer a loan to those under 20 or over 65");
        }

        if (scoringData.employment().workExperienceTotal() < 18 || scoringData.employment().workExperienceCurrent() < 3) {
            LOG.debug("Can't check further and throwing exception because scoringData.employment.workExperienceTotal = %s is less 18 months or scoringData.employment.workExperienceCurrent = %s is less 3 months"
                    .formatted(scoringData.employment().workExperienceTotal(), scoringData.employment().workExperienceCurrent()));
            throw new ForbiddenException("Cannot offer a loan to those whose total experience is less 18 months or whose current experience is less 3 months");
        }

        LOG.debug("Finished checking scoring data");
    }

    @Override
    public BigDecimal calculateAmount(final @NotNull BigDecimal amount,
                                      final boolean isInsuranceEnabled,
                                      final boolean isSalaryClient) {
        LOG.debug("Starting to calculate total amount of loan: amount = %s".formatted(amount));

        BigDecimal totalAmount = amount;

        totalAmount = totalAmount.add(isInsuranceEnabled ? amount.multiply(BigDecimal.valueOf(6).divide(BigDecimal.valueOf(100), calculationMathContext),
                        calculationMathContext) : BigDecimal.ZERO, calculationMathContext);

        LOG.debug("Calculating of the impact of the presence of insurance on the loan amount: totalAmount = %s".formatted(totalAmount));

        totalAmount = totalAmount
                .add(isSalaryClient ? amount.multiply(BigDecimal.valueOf(0).divide(BigDecimal.valueOf(100), calculationMathContext),
                        calculationMathContext) : BigDecimal.ZERO, calculationMathContext);

        LOG.debug("Calculating of the impact of the presence of salary client on the loan amount: totalAmount = %s".formatted(totalAmount));
        LOG.debug("Finished calculating total amount of loan: totalAmount = %s".formatted(totalAmount));

        return totalAmount.setScale(resultMathContext.getPrecision(), resultMathContext.getRoundingMode());
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
        LOG.debug("Starting to calculate amount of monthly payment: amount = %s, rate = %s, term = %d".formatted(amount, rate, term));

        final BigDecimal monthlyRate = rate.divide(BigDecimal.valueOf(100), calculationMathContext)
                .divide(BigDecimal.valueOf(12), calculationMathContext);

        LOG.debug("Calculating percentage of monthly loan rate: monthlyRate = %s".formatted(monthlyRate));

        final BigDecimal monthlyPayment = amount.multiply(
                monthlyRate.divide(
                        BigDecimal.ONE.subtract(
                                BigDecimal.ONE.add(monthlyRate, calculationMathContext)
                                        .pow(-term, calculationMathContext)
                        ), calculationMathContext
                ), calculationMathContext
        );

        LOG.debug("Finished calculating monthly payment: monthlyPayment = %s".formatted(monthlyPayment));

        return monthlyPayment.setScale(resultMathContext.getPrecision(), resultMathContext.getRoundingMode());
    }

    @Override
    public List<PaymentScheduleElementDto> calculatePaymentSchedule(final @NotNull BigDecimal amount,
                                                                    final @NotNull BigDecimal rate,
                                                                    final @NotNull BigDecimal monthlyPayment,
                                                                    final int term,
                                                                    final @NotNull LocalDate loanStartDate) {
        LOG.debug("Starting to calculate loan payment schedule: amount = %s, rate = %s, monthlyPayment = %s, term = %d, loanStartDate = %s"
                .formatted(amount,
                        rate,
                        monthlyPayment,
                        term,
                        loanStartDate));

        final PaymentScheduleElementDto[] paymentScheduleElements = new PaymentScheduleElementDto[term];
        final BigDecimal monthlyRate = rate.divide(BigDecimal.valueOf(100), calculationMathContext)
                .divide(BigDecimal.valueOf(12), calculationMathContext);

        LOG.debug("Calculating percentage of monthly loan rate: monthlyRate = %s".formatted(monthlyRate));

        BigDecimal currentAmount = amount;
        BigDecimal totalPayment = BigDecimal.ZERO;
        LocalDate currentDate = loanStartDate;

        for (int i = 0; i < term; i++) {

            LOG.debug("Starting to generate payment schedule elements: currentAmount = %s, totalPayment = %s, currentDate = %s"
                    .formatted(currentAmount, term, currentDate));

            final PaymentScheduleElementDto paymentScheduleElement = calculatePaymentScheduleElement(
                    i + 1,
                    currentDate,
                    currentAmount,
                    monthlyRate,
                    monthlyPayment,
                    totalPayment
            );

            LOG.debug("Generated schedule element: paymentScheduleElement = %s".formatted(paymentScheduleElement));

            currentAmount = paymentScheduleElement.remainingDebt();
            currentDate = currentDate.plusMonths(1);
            totalPayment = paymentScheduleElement.totalPayment();


            paymentScheduleElements[i] = paymentScheduleElement;
        }

        LOG.debug("Finished generating payment schedule: paymentScheduleElements = %s".formatted(Arrays.toString(paymentScheduleElements)));

        return List.of(paymentScheduleElements);
    }

    private PaymentScheduleElementDto calculatePaymentScheduleElement(final int number,
                                                                      final @NotNull LocalDate date,
                                                                      final @NotNull BigDecimal amount,
                                                                      final @NotNull BigDecimal monthlyRate,
                                                                      final @NotNull BigDecimal monthlyPayment,
                                                                      final @NotNull BigDecimal totalPayment) {
        LOG.debug("Starting to calculate payment schedule element: number = %d, date = %s, amount = %s, monthlyRate = %s, totalPayment = %s");

        final BigDecimal currentTotalPayment = totalPayment.add(monthlyPayment, calculationMathContext);
        final BigDecimal interestPayment = amount.multiply(monthlyRate, calculationMathContext);
        final BigDecimal remainingDebt = amount.add(interestPayment, calculationMathContext).subtract(monthlyPayment, calculationMathContext);
        final BigDecimal debtPayment = monthlyPayment.subtract(interestPayment, calculationMathContext);

        final PaymentScheduleElementDto scheduleElement = new PaymentScheduleElementDto(number,
                date,
                currentTotalPayment.setScale(resultMathContext.getPrecision(), resultMathContext.getRoundingMode()),
                interestPayment.setScale(resultMathContext.getPrecision(), resultMathContext.getRoundingMode()),
                debtPayment.setScale(resultMathContext.getPrecision(), resultMathContext.getRoundingMode()),
                remainingDebt.setScale(resultMathContext.getPrecision(), resultMathContext.getRoundingMode())
        );

        LOG.debug("Finished calculating payment schedule element: scheduleElement = %s".formatted(scheduleElement));

        return scheduleElement;
    }

    @Override
    public BigDecimal calculatePreScoreRate(final boolean isInsuranceEnabled,
                                            final boolean isSalaryClient) {
        LOG.debug("Starting to calculate loan pre score rate: baseRate = %s".formatted(baseRate));

        BigDecimal rate = baseRate;

        rate = rate.subtract(isInsuranceEnabled ? insuranceRate : BigDecimal.ZERO, calculationMathContext);

        LOG.debug("Calculating of the impact of the presence of insurance on the loan rate: rate = %s".formatted(rate));


        rate = rate.subtract(isSalaryClient ? salaryRate : BigDecimal.ZERO, calculationMathContext);

        LOG.debug("Calculating of the impact of the presence of salary client on the rate: rate = %s".formatted(rate));
        LOG.debug("Finished calculating loan rate: rate = %s".formatted(rate));

        return rate.setScale(resultMathContext.getPrecision(), resultMathContext.getRoundingMode());
    }

    @Override
    public BigDecimal calculatePsk(final @NotNull BigDecimal amount,
                                   final @NotNull BigDecimal monthPayment,
                                   final @NotNull Integer term) {
        LOG.debug("Starting to calculate loan psk: monthPayment = %s, term = %d".formatted(monthPayment, term));

        final BigDecimal psk = (
                monthPayment.multiply(BigDecimal.valueOf(term), calculationMathContext)
                        .divide(amount.subtract(BigDecimal.ONE, calculationMathContext), calculationMathContext))
                        .divide(BigDecimal.valueOf(term).divide(BigDecimal.valueOf(12), calculationMathContext), calculationMathContext);

        LOG.debug("Finished calculating psk: psk = %s".formatted(psk));

        return psk.setScale(resultMathContext.getPrecision(), resultMathContext.getRoundingMode());
    }


    @Override
    public BigDecimal calculateScoreRate(final @NotNull ScoringDataDto scoringData) {
        LOG.debug("Starting to calculate loan pre score rate: baseRate = %s".formatted(baseRate));

        BigDecimal rate = baseRate;

        switch (scoringData.employment().employmentStatus()) {
            case SELF_EMPLOYED -> rate = rate.add(BigDecimal.valueOf(2), calculationMathContext);
            case BUSINESS_OWNER -> rate = rate.add(BigDecimal.valueOf(3), calculationMathContext);
        }

        LOG.debug("Calculating of the impact of the presence of employment status on the loan rate: rate = %s".formatted(rate));


        switch (scoringData.employment().position()) {
            case MIDDLE_MANAGER -> rate = rate.subtract(BigDecimal.valueOf(2), calculationMathContext);
            case TOP_MANAGER -> rate = rate.subtract(BigDecimal.valueOf(3), calculationMathContext);
        }

        LOG.debug("Calculating of the impact of the presence of position on the loan rate: rate = %s".formatted(rate));


        switch (scoringData.maritalStatus()) {
            case MARRIED -> rate = rate.subtract(BigDecimal.valueOf(3), calculationMathContext);
            case DIVORCED -> rate = rate.add(BigDecimal.ONE, calculationMathContext);
        }

        LOG.debug("Calculating of the impact of the presence of marital status on the loan rate: rate = %s".formatted(rate));


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
        LOG.debug("Calculating of the impact of the presence of gender on the loan rate: rate = %s".formatted(rate));


        rate = rate.subtract(scoringData.isInsuranceEnabled() ? insuranceRate :BigDecimal.ZERO, calculationMathContext);

        LOG.debug("Calculating of the impact of the presence of insurance on the loan rate: rate = %s".formatted(rate));

        rate = rate.subtract(scoringData.isSalaryClient() ? salaryRate : BigDecimal.ZERO, calculationMathContext);

        LOG.debug("Calculating of the impact of the presence of salary client on the rate: rate = %s".formatted(rate));
        LOG.debug("Finished calculating loan rate: rate = %s".formatted(rate));

        return rate.setScale(resultMathContext.getPrecision(), resultMathContext.getRoundingMode());
    }
}
