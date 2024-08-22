package ru.chainichek.neostudy.dossier.dto.admin;

import ru.chainichek.neostudy.dossier.dto.calculation.PaymentScheduleElementDto;
import ru.chainichek.neostudy.dossier.model.credit.CreditStatus;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public record CreditDto(
        UUID id,
        BigDecimal amount,
        Integer term,
        BigDecimal monthlyPayment,
        BigDecimal rate,
        BigDecimal psk,
        List<PaymentScheduleElementDto> paymentSchedule,
        Boolean isInsuranceEnabled,
        Boolean isSalaryClient,
        CreditStatus creditStatus
) {}