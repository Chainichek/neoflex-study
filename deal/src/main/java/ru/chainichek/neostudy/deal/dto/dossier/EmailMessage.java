package ru.chainichek.neostudy.deal.dto.dossier;

import ru.chainichek.neostudy.deal.model.dossier.EmailTheme;

import java.util.UUID;

public record EmailMessage(String address,
                           EmailTheme theme,
                           UUID statementId,
                           String payload,
                           ClientDto recipient) {
}