package ru.chainichek.neostudy.deal.service;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.chainichek.neostudy.deal.dto.offer.LoanStatementRequestDto;
import ru.chainichek.neostudy.deal.dto.statement.FinishRegistrationRequestDto;
import ru.chainichek.neostudy.deal.model.client.Client;
import ru.chainichek.neostudy.deal.model.client.Employment;
import ru.chainichek.neostudy.deal.model.client.Passport;
import ru.chainichek.neostudy.deal.repo.ClientRepository;

@Service
@AllArgsConstructor
public class ClientService {
    private final ClientRepository clientRepository;

    @Transactional
    public Client createClient(@NotNull LoanStatementRequestDto loanStatementRequest) {
        return clientRepository.save(new Client(loanStatementRequest.firstName(),
                loanStatementRequest.lastName(),
                loanStatementRequest.middleName(),
                loanStatementRequest.birthdate(),
                loanStatementRequest.email(),
                new Passport(loanStatementRequest.passportSeries(),
                        loanStatementRequest.passportNumber())
        ));
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public Client updateClientOnFinishRegistration(@NotNull Client client,
                                                   @NotNull FinishRegistrationRequestDto finishRegistrationRequest) {
        client.setGender(finishRegistrationRequest.gender());
        client.setMaritalStatus(finishRegistrationRequest.maritalStatus());
        client.setDependentAmount(finishRegistrationRequest.dependentAmount());

        final Passport passport = client.getPassport();
        passport.setIssueDate(finishRegistrationRequest.passportIssueDate());
        passport.setIssueBranch(finishRegistrationRequest.passportIssueBranch());

        final Employment employment = new Employment(finishRegistrationRequest.employment().employmentStatus(),
                finishRegistrationRequest.employment().employerINN(),
                finishRegistrationRequest.employment().salary(),
                finishRegistrationRequest.employment().position(),
                finishRegistrationRequest.employment().workExperienceTotal(),
                finishRegistrationRequest.employment().workExperienceCurrent());

        client.setEmployment(employment);
        client.setAccountNumber(finishRegistrationRequest.accountNumber());

        return clientRepository.save(client);
    }
}
