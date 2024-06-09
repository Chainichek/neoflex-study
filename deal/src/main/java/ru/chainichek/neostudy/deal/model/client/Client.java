package ru.chainichek.neostudy.deal.model.client;

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
import org.springframework.format.annotation.DateTimeFormat;
import ru.chainichek.neostudy.deal.util.validation.Validation;

import java.time.LocalDate;
import java.util.UUID;

import static ru.chainichek.neostudy.deal.util.validation.client.ClientValidation.EMAIL_SIZE_MAX;
import static ru.chainichek.neostudy.deal.util.validation.client.ClientValidation.NAME_SIZE_MAX;

@Getter
@Setter
@Entity
@Table(name = "client")
@NoArgsConstructor
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(length = NAME_SIZE_MAX, nullable = false)
    private String firstName;

    @Column(length = NAME_SIZE_MAX, nullable = false)
    private String lastName;

    @Column(length = NAME_SIZE_MAX)
    private String middleName;

    @Column(nullable = false)
    @DateTimeFormat(pattern = Validation.DATE_FORMAT_PATTERN)
    private LocalDate birthdate;

    @Column(length = EMAIL_SIZE_MAX, nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Enumerated(EnumType.STRING)
    private MaritalStatus maritalStatus;

    private Integer dependentAmount;

    @Type(JsonType.class)
    @Column(nullable = false)
    private Passport passport;

    @Type(JsonType.class)
    private Employment employment;

    private String accountNumber;

    public Client(String lastName,
                  String firstName,
                  String middleName,
                  LocalDate birthdate,
                  String email,
                  Passport passport) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.middleName = middleName;
        this.birthdate = birthdate;
        this.email = email;
        this.passport = passport;
    }
}