package ru.chainichek.neostudy.deal.dto.util;

import ru.chainichek.neostudy.deal.model.dossier.EmailTheme;

import java.util.UUID;

public record EmailMessage(String address,
                           EmailTheme theme,
                           UUID statementId) {
}