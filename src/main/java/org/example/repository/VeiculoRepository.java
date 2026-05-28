package org.example.repository;

import org.example.model.Veiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface VeiculoRepository extends JpaRepository<Veiculo, UUID> {
    @Query("SELECT v FROM Veiculo v WHERE v.cliente.oficina.id = :oficinaId")
    List<Veiculo> findByOficinaId(@Param("oficinaId") UUID oficinaId);
}