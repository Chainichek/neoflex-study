package ru.chainichek.neostudy.deal.dto.offer;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.util.UUID;

public record LoanOfferDto(@NotNull
                           UUID statementId,
                           @NotNull
                           BigDecimal requestedAmount,
                           @NotNull
                           BigDecimal totalAmount,
                           @NotNull
                           Integer term,
                           @NotNull
                           BigDecimal monthlyPayment,
                           @NotNull
                           BigDecimal rate,
                           @NotNull
                           Boolean isInsuranceEnabled,
                           @NotNull
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
