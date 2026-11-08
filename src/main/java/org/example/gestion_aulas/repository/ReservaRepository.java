package org.example.gestion_aulas.repository;

import org.example.gestion_aulas.entity.FranjaHoraria;
import org.example.gestion_aulas.entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservaRepository extends JpaRepository<Reserva, Long> {
    boolean existsByAulaIdAndFechaReservaAndFranjaHorariaId(
            Long aulaId,
            LocalDate fechaReserva,
            Long franjaHorariaId
    );
}