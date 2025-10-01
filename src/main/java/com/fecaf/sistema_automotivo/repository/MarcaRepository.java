package com.fecaf.sistema_automotivo.repository;

import com.fecaf.sistema_automotivo.model.Marca;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarcaRepository extends JpaRepository<Marca, Long> {
}