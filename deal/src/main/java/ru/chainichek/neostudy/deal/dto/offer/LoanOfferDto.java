package ru.chainichek.neostudy.deal.dto.offer;

import java.math.BigDecimal;
import java.util.UUID;

public record LoanOfferDto(UUID statementId,
                           BigDecimal requestedAmount,
                           BigDecimal totalAmount,
                           Integer term,
                           BigDecimal monthlyPayment,
                           BigDecimal rate,
                           Boolean isInsuranceEnabled,
                           Boolean isSalaryClient) {
    public LoanOfferDto withStatementId(UUID statementId) {
        return new LoanOfferDto(statementId,
                this.requestedAmount,
                this.totalAmount,
                this.term,
                this.monthlyPayment,
                this.rate,
                this.isInsuranceEnabled,
                this.isSalaryClient);
    }
}
