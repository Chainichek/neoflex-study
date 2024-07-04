package ru.chainichek.neostudy.deal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.chainichek.neostudy.deal.dto.calculation.CreditDto;
import ru.chainichek.neostudy.deal.dto.dossier.ClientDossier;
import ru.chainichek.neostudy.deal.dto.dossier.EmailMessage;
import ru.chainichek.neostudy.deal.model.client.Client;
import ru.chainichek.neostudy.deal.model.credit.Credit;
import ru.chainichek.neostudy.deal.model.dossier.EmailTheme;
import ru.chainichek.neostudy.deal.model.statement.Statement;

@Mapper
public interface DossierMapper {
    ClientDossier mapToClientDossier(Client client);
    CreditDto mapToCreditDto(Credit credit);

    @Mapping(target = "statementId", source = "statement.id")
    @Mapping(target = "address", source = "statement.client.email")
    @Mapping(target = "recipient", source = "statement.client")
    EmailMessage mapToEmailMessage(Statement statement, EmailTheme theme, String payload);
}
