package com.fecaf.sistema_automotivo.repository;

import com.fecaf.sistema_automotivo.model.Modelo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ModeloRepository extends JpaRepository<Modelo, Long> {
}