package org.example.gestion_aulas.controller;

import org.example.gestion_aulas.entity.FranjaHoraria;
import org.example.gestion_aulas.repository.FranjaHorariaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class FranjaHorariaController {

    @Autowired
    private final FranjaHorariaRepository franjaHorariaRepository;

    public FranjaHorariaController(FranjaHorariaRepository franjaHorariaRepository) {
        this.franjaHorariaRepository = franjaHorariaRepository;
    }

    @PostMapping("/franjas-horarias")
    public ResponseEntity<FranjaHoraria> create(@RequestBody FranjaHoraria franjaHoraria){
        return ResponseEntity.status(HttpStatus.CREATED).body(franjaHorariaRepository.save(franjaHoraria));
    }

    @GetMapping("/franjas-horarias")
    public List<FranjaHoraria> findAll() {
        return franjaHorariaRepository.findAll();
    }

    @GetMapping("/franjas-horarias/{id}")
    public ResponseEntity<FranjaHoraria> findById(@PathVariable Long id){
        if(!franjaHorariaRepository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(franjaHorariaRepository.findById(id).get());
    }

    @DeleteMapping("/franjas-horarias/{id}")
    public ResponseEntity<FranjaHoraria> deleteById(@PathVariable Long id){
        if(!franjaHorariaRepository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        franjaHorariaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/franjas-horarias/{id}")
    public ResponseEntity<FranjaHoraria> updateById(@PathVariable Long id, @RequestBody FranjaHoraria franjaHoraria){
        if(!franjaHorariaRepository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        franjaHoraria.setId(id);
        return ResponseEntity.ok(franjaHorariaRepository.save(franjaHoraria));
    }
}