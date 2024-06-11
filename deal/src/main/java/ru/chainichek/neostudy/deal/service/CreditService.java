package ru.chainichek.neostudy.deal.service;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.chainichek.neostudy.deal.dto.calculation.CreditDto;
import ru.chainichek.neostudy.deal.model.credit.Credit;
import ru.chainichek.neostudy.deal.model.credit.CreditStatus;
import ru.chainichek.neostudy.deal.repo.CreditRepository;

@Service
@AllArgsConstructor
public class CreditService {
    private final static Logger LOG = LoggerFactory.getLogger(CreditService.class);

    private final CreditRepository creditRepository;

    @Transactional
    public Credit createCredit(@NotNull CreditDto creditDto) {
        final Credit credit = creditRepository.save(new Credit(creditDto.amount(),
                creditDto.term(),
                creditDto.monthlyPayment(),
                creditDto.rate(),
                creditDto.psk(),
                creditDto.paymentSchedule(),
                creditDto.isInsuranceEnabled(),
                creditDto.isSalaryClient(),
                CreditStatus.CALCULATED
        ));

        LOG.debug("Created a credit: creditId = %s".formatted(credit.getId()));

        return credit;
    }
}
