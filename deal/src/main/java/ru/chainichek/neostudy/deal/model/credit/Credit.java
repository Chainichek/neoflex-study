package ru.chainichek.neostudy.deal.model.credit;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import ru.chainichek.neostudy.deal.dto.calculation.PaymentScheduleElementDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "credit")
@NoArgsConstructor
public class Credit {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;

    @Column(nullable = false)
    private Integer term;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal monthlyPayment;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal rate;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal psk;

    @Type(JsonType.class)
    private List<PaymentScheduleElementDto> paymentSchedule;

    @Column(name = "insurance_enabled", nullable = false)
    private Boolean isInsuranceEnabled = false;

    @Column(name = "salary_client", nullable = false)
    private Boolean isSalaryClient = false;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CreditStatus creditStatus;

    public Credit(BigDecimal amount,
                  Integer term,
                  BigDecimal monthlyPayment,
                  BigDecimal rate,
                  BigDecimal psk,
                  List<PaymentScheduleElementDto> paymentSchedule,
                  Boolean isInsuranceEnabled,
                  Boolean isSalaryClient,
                  CreditStatus creditStatus) {
        this.amount = amount;
        this.term = term;
        this.monthlyPayment = monthlyPayment;
        this.rate = rate;
        this.psk = psk;
        this.paymentSchedule = paymentSchedule;
        this.isInsuranceEnabled = isInsuranceEnabled;
        this.isSalaryClient = isSalaryClient;
        this.creditStatus = creditStatus;
    }
}