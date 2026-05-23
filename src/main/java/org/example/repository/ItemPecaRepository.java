package org.example.repository;

import org.example.model.ItemPeca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ItemPecaRepository extends JpaRepository<ItemPeca, UUID> {
}
