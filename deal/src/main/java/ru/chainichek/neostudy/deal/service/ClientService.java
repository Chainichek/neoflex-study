package ru.chainichek.neostudy.deal.service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.chainichek.neostudy.deal.dto.offer.LoanStatementRequestDto;
import ru.chainichek.neostudy.deal.dto.statement.FinishRegistrationRequestDto;
import ru.chainichek.neostudy.deal.mapper.ClientMapper;
import ru.chainichek.neostudy.deal.model.client.Client;
import ru.chainichek.neostudy.deal.repo.ClientRepository;

@Service
@AllArgsConstructor
public class ClientService {
    private final static Logger LOG = LoggerFactory.getLogger(ClientService.class);

    private final ClientRepository clientRepository;

    private final ClientMapper clientMapper;


    public Client createClient(@NonNull LoanStatementRequestDto request) {
        final Client client = clientRepository.save(clientMapper.mapToClient(request));

        LOG.debug("Created a client: clientId = %s".formatted(client.getId()));

        return client;
    }

    @Transactional(Transactional.TxType.REQUIRES_NEW)
    public Client updateClientOnFinishRegistration(@NonNull Client client,
                                                   @NonNull FinishRegistrationRequestDto finishRegistrationRequest) {
        LOG.debug("Interrupting statement update transaction with a new one in order to update client credentials: clientId = %s"
                .formatted(client.getId()));

        final Client updatedClient = clientRepository.save(clientMapper.updateClient(client, finishRegistrationRequest));

        LOG.debug("Updated a client, continuing the transaction: clientId = %s"
                .formatted(updatedClient.getId()));

        return updatedClient;
    }
}
