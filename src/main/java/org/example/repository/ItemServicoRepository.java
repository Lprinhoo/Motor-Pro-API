package org.example.repository;

import org.example.model.ItemServico;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ItemServicoRepository extends JpaRepository<ItemServico, UUID> {
}
