package ru.chainichek.neostudy.deal.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.chainichek.neostudy.deal.api.DocumentApi;

import java.util.UUID;

@RestController
@AllArgsConstructor
public class DocumentController implements DocumentApi {
    @Override
    public ResponseEntity<Void> sendDocuments(UUID statementId) {
        return null;
    }

    @Override
    public ResponseEntity<Void> signDocuments(UUID statementId) {
        return null;
    }

    @Override
    public ResponseEntity<Void> verifyCode(UUID statementId, String code) {
        return null;
    }
}
