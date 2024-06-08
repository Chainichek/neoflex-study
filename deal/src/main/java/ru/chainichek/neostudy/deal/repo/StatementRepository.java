package ru.chainichek.neostudy.deal.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.chainichek.neostudy.deal.model.statement.Statement;

import java.util.UUID;

public interface StatementRepository extends JpaRepository<Statement, UUID> {
}