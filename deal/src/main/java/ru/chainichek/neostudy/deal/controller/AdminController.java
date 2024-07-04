package ru.chainichek.neostudy.deal.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.chainichek.neostudy.deal.api.AdminApi;
import ru.chainichek.neostudy.deal.model.statement.ApplicationStatus;

@RestController
@AllArgsConstructor
public class AdminController implements AdminApi {
    @Override
    public ResponseEntity<Void> updateStatus(long statementId, ApplicationStatus status) {
        return null;
    }
}
