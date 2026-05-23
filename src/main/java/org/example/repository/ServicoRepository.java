package org.example.repository;

import org.example.model.Servico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ServicoRepository extends JpaRepository<Servico, UUID> {
}
