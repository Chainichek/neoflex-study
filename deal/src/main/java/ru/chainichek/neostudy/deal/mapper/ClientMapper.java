package ru.chainichek.neostudy.deal.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValueCheckStrategy;
import org.mapstruct.NullValuePropertyMappingStrategy;
import ru.chainichek.neostudy.deal.dto.offer.LoanStatementRequestDto;
import ru.chainichek.neostudy.deal.dto.statement.EmploymentDto;
import ru.chainichek.neostudy.deal.dto.statement.FinishRegistrationRequestDto;
import ru.chainichek.neostudy.deal.model.client.Client;
import ru.chainichek.neostudy.deal.model.client.Employment;
import ru.chainichek.neostudy.deal.model.client.Passport;

@Mapper
public interface ClientMapper {
    @Mapping(target = "series", source = "passportSeries")
    @Mapping(target = "number", source = "passportNumber")
    @Mapping(target = "issueDate", ignore = true)
    @Mapping(target = "issueBranch", ignore = true)
    @Mapping(target = "id", ignore = true)
    Passport mapToPassport(LoanStatementRequestDto loanStatementRequestDto);

    @Mapping(target = "passport", source = "loanStatementRequestDto")
    @Mapping(target = "maritalStatus", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "gender", ignore = true)
    @Mapping(target = "employment", ignore = true)
    @Mapping(target = "dependentAmount", ignore = true)
    @Mapping(target = "accountNumber", ignore = true)
    Client mapToClient(LoanStatementRequestDto loanStatementRequestDto);


    @Mapping(target = "status", source = "employmentStatus")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "employerInn", source = "employerINN")
    Employment mapToEmployment(EmploymentDto employmentDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
            nullValueCheckStrategy = NullValueCheckStrategy.ALWAYS)
    @Mapping(target = "passport.issueBranch", source = "finishRegistrationRequestDto.passportIssueBranch")
    @Mapping(target = "passport.issueDate", source = "finishRegistrationRequestDto.passportIssueDate")
    @Mapping(target = "middleName", ignore = true)
    @Mapping(target = "lastName", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "firstName", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "birthdate", ignore = true)
    Client updateClient(@MappingTarget Client client, FinishRegistrationRequestDto finishRegistrationRequestDto);
}
