package org.example.repository;

import org.example.model.Oficina;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional; // Import necessário
import java.util.UUID;

@Repository
public interface OficinaRepository extends JpaRepository<Oficina, UUID> {
    Optional<Oficina> findByUserUsername(String username); // Linha adicionada
}