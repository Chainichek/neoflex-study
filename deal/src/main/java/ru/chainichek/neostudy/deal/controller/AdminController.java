package ru.chainichek.neostudy.deal.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.chainichek.neostudy.deal.api.AdminApi;
import ru.chainichek.neostudy.deal.dto.admin.StatementDto;
import ru.chainichek.neostudy.deal.model.statement.ApplicationStatus;
import ru.chainichek.neostudy.deal.service.AdminService;
import ru.chainichek.neostudy.lib.loggerutils.annotation.ControllerLoggable;

import java.util.UUID;

@RestController
@AllArgsConstructor
@ControllerLoggable
public class AdminController implements AdminApi {
    private final AdminService adminService;

    @Override
    public ResponseEntity<StatementDto> getStatement(UUID statementId) {
        return ResponseEntity.ok(adminService.getStatement(statementId));
    }

    @Override
    public ResponseEntity<Void> updateStatus(UUID statementId, ApplicationStatus status) {
        adminService.updateStatus(statementId, status);
        return ResponseEntity.ok().build();
    }
}
