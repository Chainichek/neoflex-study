package ru.chainichek.neostudy.dossier.dto.message;


import ru.chainichek.neostudy.dossier.model.dossier.EmailTheme;

import java.util.UUID;

public record EmailMessage(String address,
                           EmailTheme theme,
                           UUID statementId) {
}