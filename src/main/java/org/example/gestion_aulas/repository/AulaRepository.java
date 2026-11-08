package org.example.gestion_aulas.repository;

import org.example.gestion_aulas.entity.Aula;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AulaRepository extends JpaRepository<Aula, Long> {
    List<Aula> findByTieneOrdenadores(Boolean tieneOrdenadores);

    @Query("SELECT a FROM Aula a WHERE a.capacidad > :capacidadMin")
    List<Aula> findByCapacidadMin(int capacidadMin);
}
