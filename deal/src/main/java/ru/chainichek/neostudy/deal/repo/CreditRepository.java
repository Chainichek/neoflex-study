package ru.chainichek.neostudy.deal.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.chainichek.neostudy.deal.model.credit.Credit;

import java.util.UUID;

public interface CreditRepository extends JpaRepository<Credit, UUID> {
}