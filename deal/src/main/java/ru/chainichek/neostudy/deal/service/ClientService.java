package ru.chainichek.neostudy.deal.service;

import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private final static Logger LOG = LoggerFactory.getLogger(ClientService.class);

    private final ClientRepository clientRepository;

    @Transactional
    public Client createClient(@NotNull LoanStatementRequestDto request) {
        final Client client = clientRepository.save(new Client(request.firstName(),
                request.lastName(),
                request.middleName(),
                request.birthdate(),
                request.email(),
                new Passport(request.passportSeries(),
                        request.passportNumber())
        ));

        LOG.debug("Created a client: clientId = %s".formatted(client.getId()));

        return client;
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public Client updateClientOnFinishRegistration(@NotNull Client client,
                                                   @NotNull FinishRegistrationRequestDto finishRegistrationRequest) {
        LOG.debug("Interrupting statement update transaction with a new one in order to update client credentials: clientId = %s"
                .formatted(client.getId()));

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

        final Client updatedClient = clientRepository.save(client);

        LOG.debug("Updated a client, continuing the transaction: clientId = %s"
                .formatted(updatedClient.getId()));

        return updatedClient;
    }
}
