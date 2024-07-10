package ru.chainichek.neostudy.deal.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.chainichek.neostudy.deal.service.DocumentService;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class DocumentControllerTest {
    @InjectMocks
    DocumentController documentController;

    @Mock
    DocumentService documentService;
    @Mock
    UUID statementId;

    @Test
    void sendDocuments() {
        documentController.sendDocuments(statementId);

        verify(documentService).sendDocuments(eq(statementId));
    }

    @Test
    void signDocuments() {
        documentController.signDocuments(statementId);

        verify(documentService).signDocuments(eq(statementId));
    }

    @Test
    void verifyCode() {
        documentController.verifyCode(statementId, "");

        verify(documentService).verifyCode(eq(statementId), eq(""));
    }
}