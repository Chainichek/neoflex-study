package ru.chainichek.neostudy.dossier.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import ru.chainichek.neostudy.dossier.dto.admin.ClientDto;
import ru.chainichek.neostudy.dossier.dto.admin.CreditDto;
import ru.chainichek.neostudy.dossier.dto.admin.PassportDto;
import ru.chainichek.neostudy.dossier.dto.admin.StatementDto;
import ru.chainichek.neostudy.dossier.dto.calculation.PaymentScheduleElementDto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DocumentGeneratorServiceTest {
    @InjectMocks
    DocumentGeneratorService documentGeneratorService;
    @Mock
    MessageSource documentMessageSource;

    @Test
    void generateDocument_ifStatementIsCorrect_returnByteArray() {
        StatementDto statement = mock(StatementDto.class);
        ClientDto client = mock(ClientDto.class);
        PassportDto passport = mock(PassportDto.class);
        CreditDto credit = mock(CreditDto.class);
        PaymentScheduleElementDto paymentScheduleElement = mock(PaymentScheduleElementDto.class);
        List<PaymentScheduleElementDto> paymentSchedule = List.of(
                paymentScheduleElement,
                paymentScheduleElement,
                paymentScheduleElement
        );

        when(statement.credit()).thenReturn(credit);
        when(statement.client()).thenReturn(client);
        when(client.passport()).thenReturn(passport);
        when(credit.paymentSchedule()).thenReturn(paymentSchedule);

        when(paymentScheduleElement.number()).thenReturn(1);
        when(paymentScheduleElement.date()).thenReturn(LocalDate.now());
        when(paymentScheduleElement.debtPayment()).thenReturn(BigDecimal.ZERO);
        when(paymentScheduleElement.interestPayment()).thenReturn(BigDecimal.ZERO);
        when(paymentScheduleElement.totalPayment()).thenReturn(BigDecimal.ZERO);
        when(paymentScheduleElement.remainingDebt()).thenReturn(BigDecimal.ZERO);

        when(documentMessageSource.getMessage(any(), any(), any())).thenReturn("");

        byte[] result = documentGeneratorService.generateDocument(statement);

        assertNotEquals(0, result.length);

        verify(documentMessageSource, times(23)).getMessage(any(), any(), any());
    }

    @Test
    void generateDocument_ifStatementIsNotCorrect_throwIllegalArgumentException() {
        StatementDto statement = mock(StatementDto.class);

        assertThrows(IllegalArgumentException.class, () -> documentGeneratorService.generateDocument(statement));

        verify(documentMessageSource, never()).getMessage(any(), any(), any());
    }
}