package ru.chainichek.neostudy.deal.service;

import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.chainichek.neostudy.deal.client.CalculatorClient;
import ru.chainichek.neostudy.deal.dto.calculation.CreditDto;
import ru.chainichek.neostudy.deal.dto.calculation.ScoringDataDto;
import ru.chainichek.neostudy.deal.dto.offer.LoanOfferDto;
import ru.chainichek.neostudy.deal.dto.offer.LoanStatementRequestDto;
import ru.chainichek.neostudy.deal.dto.statement.EmploymentDto;
import ru.chainichek.neostudy.deal.mapper.CalculatorMapper;
import ru.chainichek.neostudy.deal.model.client.Client;
import ru.chainichek.neostudy.deal.model.client.Passport;
import ru.chainichek.neostudy.deal.model.statement.Statement;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CalculatorService {
    private final static Logger LOG = LoggerFactory.getLogger(CalculatorService.class);

    private final CalculatorClient calculatorClient;

    private final CalculatorMapper calculatorMapper;

    public List<LoanOfferDto> getOffers(LoanStatementRequestDto request, UUID statementId) {
        return calculatorClient.getOffers(request)
                .stream()
                .map(x -> x.withStatementId(statementId))
                .toList();
    }

    public CreditDto calculateCredit(Statement statement, EmploymentDto employment) {
        final Client client = statement.getClient();
        final Passport passport = client.getPassport();
        final LoanOfferDto loanOffer = statement.getAppliedOffer();

        final ScoringDataDto scoringData = calculatorMapper.mapToScoringDataDto(client, passport, loanOffer, employment);

        LOG.debug("Sending request to 'calculate credit': scoringData = %s".formatted(scoringData));

        final CreditDto credit = calculatorClient.calculateCredit(scoringData);

        LOG.debug("Received credit info: credit = %s".formatted(credit));

        return credit;
    }
}
