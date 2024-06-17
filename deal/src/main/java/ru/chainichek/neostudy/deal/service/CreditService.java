package ru.chainichek.neostudy.deal.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.chainichek.neostudy.deal.dto.calculation.CreditDto;
import ru.chainichek.neostudy.deal.mapper.CreditMapper;
import ru.chainichek.neostudy.deal.model.credit.Credit;
import ru.chainichek.neostudy.deal.model.credit.CreditStatus;
import ru.chainichek.neostudy.deal.repo.CreditRepository;

@Service
@AllArgsConstructor
public class CreditService {
    private final static Logger LOG = LoggerFactory.getLogger(CreditService.class);

    private final CreditRepository creditRepository;

    private final CreditMapper creditMapper;

    @Transactional
    public Credit createCredit(@NonNull CreditDto creditDto) {
        Credit credit = creditMapper.mapToCredit(creditDto);
        credit.setCreditStatus(CreditStatus.CALCULATED);
        credit = creditRepository.save(credit);

        LOG.debug("Created a credit: creditId = %s".formatted(credit.getId()));

        return credit;
    }
}
