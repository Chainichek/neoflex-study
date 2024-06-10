package ru.chainichek.neostudy.deal.model.statement;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import ru.chainichek.neostudy.deal.dto.offer.LoanOfferDto;
import ru.chainichek.neostudy.deal.dto.statement.StatementStatusHistoryDto;
import ru.chainichek.neostudy.deal.model.client.Client;
import ru.chainichek.neostudy.deal.model.credit.Credit;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "statement")
@NoArgsConstructor
public class Statement {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "client_id")
    private Client client;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "credit_id")
    private Credit credit;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApplicationStatus status = ApplicationStatus.PREAPPROVAL;

    @Column(nullable = false)
    private LocalDateTime creationDate = LocalDateTime.now();

    @Type(JsonType.class)
    private LoanOfferDto appliedOffer;

    private LocalDateTime signDate;

    private String sesCode;

    @Column(nullable = false)
    @Type(JsonType.class)
    private List<StatementStatusHistoryDto> statusHistory = new ArrayList<>();

    public Statement(Client client) {
        this.client = client;
    }

    public void setStatus(ApplicationStatus status) {
        this.statusHistory.add(new StatementStatusHistoryDto(this.status));
        this.status = status;
    }
}