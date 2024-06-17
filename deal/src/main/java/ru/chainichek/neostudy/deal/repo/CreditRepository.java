package ru.chainichek.neostudy.deal.repo;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.chainichek.neostudy.deal.model.credit.Credit;

import java.util.UUID;

@Transactional
public interface CreditRepository extends JpaRepository<Credit, UUID> {
}