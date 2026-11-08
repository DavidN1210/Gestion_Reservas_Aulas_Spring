package org.example.gestion_aulas.controller;

import org.example.gestion_aulas.entity.Reserva;
import org.example.gestion_aulas.repository.FranjaHorariaRepository;
import org.example.gestion_aulas.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
public class ReservaController {

    @Autowired
    private final ReservaRepository reservaRepository;

    @Autowired
    private final FranjaHorariaRepository franjaHorariaRepository;

    public ReservaController(ReservaRepository reservaRepository, FranjaHorariaRepository franjaHorariaRepository) {
        this.reservaRepository = reservaRepository;
        this.franjaHorariaRepository = franjaHorariaRepository;
    }

    @PostMapping("/reservas")
    public ResponseEntity<?> create(@RequestBody Reserva reserva){
        // 1. Validar que la fecha no sea anterior a la actual
        if (reserva.getFechaReserva().isBefore(LocalDate.now())) {
            return ResponseEntity.badRequest()
                    .body("La fecha de la reserva no puede ser anterior a la fecha actual.");
        }

        // 2. Validar si la franja ya está ocupada para esa aula y fecha concretas
        boolean ocupada = reservaRepository.existsByAulaIdAndFechaReservaAndFranjaHorariaId(
                reserva.getAula().getId(),
                reserva.getFechaReserva(),
                reserva.getFranjaHoraria().getId()
        );

        if (ocupada) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("El aula ya está reservada en esa fecha y franja horaria.");
        }

        // 3. Si pasa las validaciones, se guarda
        Reserva nuevaReserva = reservaRepository.save(reserva);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaReserva);
    }

    @GetMapping("/reservas")
    public List<Reserva> findAll() {
        return reservaRepository.findAll();
    }

    @GetMapping("/reservas/{id}")
    public ResponseEntity<Reserva> findById(@PathVariable Long id){
        if(!reservaRepository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(reservaRepository.findById(id).get());
    }

    @DeleteMapping("/reservas/{id}")
    public ResponseEntity<Reserva> deleteById(@PathVariable Long id){
        if(!reservaRepository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        reservaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/reservas/{id}")
    public ResponseEntity<Reserva> updateById(@PathVariable Long id, @RequestBody Reserva reserva){
        if(!reservaRepository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        reserva.setId(id);
        return ResponseEntity.ok(reservaRepository.save(reserva));
    }
}