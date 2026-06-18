package org.example.repository;

import org.example.model.Contato;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ContatoRepository extends JpaRepository<Contato, UUID> {
    List<Contato> findByOficinaId(UUID oficinaId);
}
