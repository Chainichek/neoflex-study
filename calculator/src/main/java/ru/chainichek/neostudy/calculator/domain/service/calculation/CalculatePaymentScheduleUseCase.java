package ru.chainichek.neostudy.calculator.domain.service.calculation;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.chainichek.neostudy.calculator.domain.entity.dto.score.PaymentScheduleElementDto;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;
import java.util.List;

@Service
public class CalculatePaymentScheduleUseCase implements PaymentScheduleCalculator {
    private final MathContext resultMathContext;
    private final MathContext calculationMathContext;

    public CalculatePaymentScheduleUseCase(final @Qualifier("resultMathContext") MathContext resultMathContext,
                                           final @Qualifier("calculationMathContext") MathContext calculationMathContext) {
        this.resultMathContext = resultMathContext;
        this.calculationMathContext = calculationMathContext;
    }

    @Override
    public List<PaymentScheduleElementDto> execute(final BigDecimal amount,
                                                   final BigDecimal rate,
                                                   final BigDecimal monthlyPayment,
                                                   final int term,
                                                   final LocalDate loanStartDate) {
        final PaymentScheduleElementDto[] paymentScheduleElements = new PaymentScheduleElementDto[term];
        final BigDecimal monthlyRate = rate.divide(BigDecimal.valueOf(100), calculationMathContext)
                .divide(BigDecimal.valueOf(12), calculationMathContext);

        BigDecimal currentAmount = amount;
        BigDecimal totalPayment = BigDecimal.ZERO;
        LocalDate currentDate = loanStartDate;

        for (int i = 0; i < term; i++) {
            final BigDecimal interestPayment = currentAmount.multiply(monthlyRate, resultMathContext);
            final BigDecimal remainingDebt = currentAmount.add(interestPayment, calculationMathContext).subtract(monthlyPayment, resultMathContext);
            final BigDecimal debtPayment = monthlyPayment.subtract(interestPayment, resultMathContext);

            currentAmount = remainingDebt;
            currentDate = currentDate.plusMonths(1);
            totalPayment = totalPayment.add(monthlyPayment, resultMathContext);

            paymentScheduleElements[i] = new PaymentScheduleElementDto(i+1,
                    currentDate,
                    totalPayment,
                    interestPayment,
                    debtPayment,
                    remainingDebt);
        }

        return List.of(paymentScheduleElements);
    }
}
