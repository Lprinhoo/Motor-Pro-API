package org.example.repository;

import org.example.model.Peca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PecaRepository extends JpaRepository<Peca, UUID> {
}
