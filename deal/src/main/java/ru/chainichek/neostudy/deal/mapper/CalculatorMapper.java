package ru.chainichek.neostudy.deal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.chainichek.neostudy.deal.dto.calculation.ScoringDataDto;
import ru.chainichek.neostudy.deal.dto.offer.LoanOfferDto;
import ru.chainichek.neostudy.deal.dto.statement.EmploymentDto;
import ru.chainichek.neostudy.deal.model.client.Client;
import ru.chainichek.neostudy.deal.model.client.Passport;

@Mapper
public interface CalculatorMapper {
    @Mapping(target = "passportSeries", source = "passport.series")
    @Mapping(target = "passportNumber", source = "passport.number")
    @Mapping(target = "passportIssueDate", source = "passport.issueDate")
    @Mapping(target = "passportIssueBranch", source = "passport.issueBranch")
    @Mapping(target = "amount", source = "loanOfferDto.totalAmount")
    @Mapping(target = "employment", source = "employmentDto")
    ScoringDataDto mapToScoringDataDto(Client client, Passport passport, LoanOfferDto loanOfferDto, EmploymentDto employmentDto);
}
