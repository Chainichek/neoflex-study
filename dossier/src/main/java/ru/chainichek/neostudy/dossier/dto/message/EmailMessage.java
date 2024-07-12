package ru.chainichek.neostudy.dossier.dto.message;


import jakarta.validation.constraints.NotNull;
import ru.chainichek.neostudy.dossier.model.dossier.EmailTheme;

import java.util.UUID;

public record EmailMessage(@NotNull
                           String address,
                           @NotNull
                           EmailTheme theme,
                           @NotNull
                           UUID statementId) {
}