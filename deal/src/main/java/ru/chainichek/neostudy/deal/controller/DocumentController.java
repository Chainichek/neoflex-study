package ru.chainichek.neostudy.deal.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.chainichek.neostudy.deal.api.DocumentApi;
import ru.chainichek.neostudy.deal.service.DocumentService;

import java.util.UUID;

@Slf4j
@RestController
@AllArgsConstructor
public class DocumentController implements DocumentApi {
    private final DocumentService documentService;
    @Override
    public ResponseEntity<Void> sendDocuments(UUID statementId) {
        log.info("Request on 'send documents' Got: statementId = {}", statementId);
        documentService.sendDocuments(statementId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> signDocuments(UUID statementId) {
        log.info("Request on 'sign documents' Got: statementId = {}", statementId);
        documentService.signDocuments(statementId);
        return ResponseEntity.ok().build();
    }

    @Override
    public ResponseEntity<Void> verifyCode(UUID statementId, String code) {
        log.info("Request on 'verify code' Got: statementId = {} code = {}", statementId, code);
        documentService.verifyCode(statementId, code);
        return ResponseEntity.ok().build();
    }
}
