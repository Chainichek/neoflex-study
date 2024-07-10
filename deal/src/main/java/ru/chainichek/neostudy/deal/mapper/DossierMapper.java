package ru.chainichek.neostudy.deal.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.chainichek.neostudy.deal.dto.message.EmailMessage;
import ru.chainichek.neostudy.deal.model.dossier.EmailTheme;
import ru.chainichek.neostudy.deal.model.statement.Statement;

@Mapper
public interface DossierMapper {
    @Mapping(target = "statementId", source = "statement.id")
    @Mapping(target = "address", source = "statement.client.email")
    EmailMessage mapToEmailMessage(Statement statement, EmailTheme theme);
}
