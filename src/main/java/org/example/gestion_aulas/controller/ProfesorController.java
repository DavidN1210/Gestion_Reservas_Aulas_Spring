package org.example.gestion_aulas.controller;

import org.example.gestion_aulas.entity.Profesor;
import org.example.gestion_aulas.repository.ProfesorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
public class ProfesorController {

    @Autowired
    private final ProfesorRepository profesorRepository;

    public ProfesorController(ProfesorRepository profesorRepository) {
        this.profesorRepository = profesorRepository;
    }

    @PostMapping("/profesores")
    public ResponseEntity<Profesor> create(@RequestBody Profesor profesor){
        return ResponseEntity.status(HttpStatus.CREATED).body(profesorRepository.save(profesor));
    }

    @GetMapping("/profesores")
    public List<Profesor> findAll() {
        return profesorRepository.findAll();
    }

    @GetMapping("/profesores/{id}")
    public ResponseEntity<Profesor> findById(@PathVariable Long id){
        if(!profesorRepository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(profesorRepository.findById(id).get());
    }

    @DeleteMapping("/profesores/{id}")
    public ResponseEntity<Profesor> deleteById(@PathVariable Long id){
        if(!profesorRepository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        profesorRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/profesores/{id}")
    public ResponseEntity<Profesor> updateById(@PathVariable Long id, @RequestBody Profesor profesor){
        if(!profesorRepository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        profesor.setId(id);
        return ResponseEntity.ok(profesorRepository.save(profesor));
    }
}