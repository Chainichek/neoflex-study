package ru.chainichek.neostudy.deal.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.chainichek.neostudy.deal.model.client.Client;

import java.util.UUID;

public interface ClientRepository extends JpaRepository<Client, UUID> {
}