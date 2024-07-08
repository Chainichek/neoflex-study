package ru.chainichek.neostudy.deal.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.chainichek.neostudy.deal.api.DocumentApi;
import ru.chainichek.neostudy.deal.service.DocumentService;
import ru.chainichek.neostudy.lib.loggerutils.annotation.ControllerLoggable;

import java.util.UUID;

@RestController
@AllArgsConstructor
@ControllerLoggable
public class DocumentController implements DocumentApi {
    private final DocumentService documentService;
    @Override
    public ResponseEntity<Void> sendDocuments(UUID statementId) {
        documentService.sendDocuments(statementId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> signDocuments(UUID statementId) {
        documentService.signDocuments(statementId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> verifyCode(UUID statementId, String code) {
        documentService.verifyCode(statementId, code);
        return ResponseEntity.ok().build();
    }
}
