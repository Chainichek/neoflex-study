package ru.chainichek.neostudy.dossier.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import ru.chainichek.neostudy.dossier.config.FeignConfig;
import ru.chainichek.neostudy.dossier.model.statement.ApplicationStatus;
import ru.chainichek.neostudy.dossier.dto.admin.StatementDto;

import java.util.UUID;

@FeignClient(name = "calculator-service",
        url = "${app.client.deal.url}",
        path = "${app.client.deal.path}",
        configuration = FeignConfig.class)
public interface DealClient {
    @RequestMapping(method = RequestMethod.GET, value = "${app.client.deal.path.get-statement}")
    StatementDto getStatement(@RequestParam("statementId") UUID statementId);
    @RequestMapping(method = RequestMethod.PUT, value = "${app.client.deal.path.update-status}")
    void updateStatus(@RequestParam("statementId") UUID statementId, @RequestBody ApplicationStatus status);
}
