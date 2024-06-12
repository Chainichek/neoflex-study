package ru.chainichek.neostudy.deal.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;
import ru.chainichek.neostudy.deal.dto.calculation.CreditDto;
import ru.chainichek.neostudy.deal.dto.calculation.PaymentScheduleElementDto;
import ru.chainichek.neostudy.deal.model.credit.Credit;
import ru.chainichek.neostudy.deal.model.credit.CreditStatus;
import ru.chainichek.neostudy.deal.repo.CreditRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreditServiceTest {
    @InjectMocks
    CreditService creditService;
    @Mock
    CreditRepository creditRepository;

    @Test
    void createCredit() {
        final CreditDto creditDto = new CreditDto(
                new BigDecimal("30000"),
                6,
                new BigDecimal("5176.45"),
                new BigDecimal("12"),
                new BigDecimal("7.06"),
                true,
                true,
                List.of(
                        new PaymentScheduleElementDto(
                                1,
                                LocalDate.of(2024, 6, 1),
                                new BigDecimal("5176.45"),
                                new BigDecimal("300"),
                                new BigDecimal("4876.45"),
                                new BigDecimal("25123.55")
                        ),
                        new PaymentScheduleElementDto(
                                2,
                                LocalDate.of(2024, 7, 1),
                                new BigDecimal("10352.9"),
                                new BigDecimal("251.24"),
                                new BigDecimal("4925.22"),
                                new BigDecimal("20198.33")
                        ),
                        new PaymentScheduleElementDto(
                                3,
                                LocalDate.of(2024, 8, 1),
                                new BigDecimal("15529.35"),
                                new BigDecimal("201.98"),
                                new BigDecimal("4974.47"),
                                new BigDecimal("15223.87")
                        ),
                        new PaymentScheduleElementDto(
                                4,
                                LocalDate.of(2024, 9, 1),
                                new BigDecimal("20705.8"),
                                new BigDecimal("152.24"),
                                new BigDecimal("5024.21"),
                                new BigDecimal("10199.65")
                        ),
                        new PaymentScheduleElementDto(
                                5,
                                LocalDate.of(2024, 10, 1),
                                new BigDecimal("25882.26"),
                                new BigDecimal("102"),
                                new BigDecimal("5074.45"),
                                new BigDecimal("5125.2")
                        ),
                        new PaymentScheduleElementDto(
                                6,
                                LocalDate.of(2024, 11, 1),
                                new BigDecimal("31058.71"),
                                new BigDecimal("51.25"),
                                new BigDecimal("5125.2"),
                                new BigDecimal("0")
                        )
                ));
        final Credit credit = new Credit(creditDto.amount(),
                creditDto.term(),
                creditDto.monthlyPayment(),
                creditDto.rate(),
                creditDto.psk(),
                creditDto.paymentSchedule(),
                creditDto.isInsuranceEnabled(),
                creditDto.isSalaryClient(),
                CreditStatus.CALCULATED
        );


        when(creditRepository.save(ArgumentMatchers.any())).thenAnswer((Answer<Credit>) invocation -> invocation.getArgument(0));

        final Credit createdCredit = creditService.createCredit(creditDto);

        assertNotNull(createdCredit);
        assertThat(credit)
                .usingRecursiveComparison()
                .isEqualTo(createdCredit);
    }
}