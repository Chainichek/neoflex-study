package ru.chainichek.neostudy.deal.model.statement;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.chainichek.neostudy.deal.dto.statement.StatementStatusHistoryDto;
import ru.chainichek.neostudy.deal.model.client.Client;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class StatementTest {

    @Test
    void setStatus() {
        final Statement statement = Statement.builder().client(mock(Client.class))
                .status(ApplicationStatus.PREAPPROVAL)
                .creationDate(LocalDateTime.now())
                .statusHistory(new ArrayList<>())
                .build();
        final StatementStatusHistoryDto statementStatusHistory = new StatementStatusHistoryDto(ApplicationStatus.PREAPPROVAL);

        statement.setStatus(ApplicationStatus.APPROVED);


        assertThat(statementStatusHistory)
                .usingRecursiveComparison()
                .ignoringFields("time")
                .isEqualTo(statement.getStatusHistory().get(0));
    }
}