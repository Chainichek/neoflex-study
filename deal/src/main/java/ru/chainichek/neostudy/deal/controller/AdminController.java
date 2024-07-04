package ru.chainichek.neostudy.deal.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.chainichek.neostudy.deal.api.AdminApi;
import ru.chainichek.neostudy.deal.model.statement.ApplicationStatus;
import ru.chainichek.neostudy.deal.service.AdminService;

import java.util.UUID;

@Slf4j
@RestController
@AllArgsConstructor
public class AdminController implements AdminApi {
    private final AdminService adminService;
    @Override
    public ResponseEntity<Void> updateStatus(UUID statementId, ApplicationStatus status) {
        log.info("Request on 'update status' Got: statementId = {} status = {}", statementId, status);
        adminService.updateStatus(statementId, status);
        return ResponseEntity.ok().build();
    }
}
