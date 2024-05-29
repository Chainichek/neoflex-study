package ru.chainichek.neostudy.calculator.service.calculation;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;

@Service
public class CalculateMonthlyPaymentUseCase implements MonthlyPaymentCalculator {
    private final MathContext resultMathContext;
    private final MathContext calculationMathContext;

    public CalculateMonthlyPaymentUseCase(final @Qualifier("resultMathContext") MathContext resultMathContext,
                                          final @Qualifier("calculationMathContext") MathContext calculationMathContext) {
        this.resultMathContext = resultMathContext;
        this.calculationMathContext = calculationMathContext;
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
    public BigDecimal execute(final BigDecimal amount,
                              final BigDecimal rate,
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
}
