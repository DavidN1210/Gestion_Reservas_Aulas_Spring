package org.example.gestion_aulas.controller;

import org.example.gestion_aulas.entity.Aula;
import org.example.gestion_aulas.repository.AulaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
public class AulaController {

    @Autowired
    private AulaRepository aulaRepository;

    public AulaController(AulaRepository aulaRepository) {
        this.aulaRepository = aulaRepository;
    }

    @PostMapping("/aulas")
    public ResponseEntity<Aula> create(@RequestBody Aula aula){
        return ResponseEntity.status(HttpStatus.CREATED).body(aulaRepository.save(aula));
    }

    @GetMapping("/aulas")
    public List<Aula> findAll() {return aulaRepository.findAll();}

    @GetMapping("/aulas/{id}")
    public ResponseEntity<Aula> findById(@PathVariable Long id){
        if(!aulaRepository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(aulaRepository.findById(id).get());
    }

    @GetMapping(value = "/aulas", params = "ordenadores")
    public ResponseEntity<List<Aula>> findByOrdenadores(@RequestParam Boolean ordenadores) {
        return ResponseEntity.ok(aulaRepository.findByTieneOrdenadores(ordenadores));
    }

    @GetMapping(value = "/aulas", params = "capacidad")
    public ResponseEntity<List<Aula>> findByCapacidad(@RequestParam Integer capacidad) {
        return ResponseEntity.ok(aulaRepository.findByCapacidadMin(capacidad));
    }

    @DeleteMapping("/aulas/{id}")
    public ResponseEntity<Aula> deleteById(@PathVariable Long id){
        if(!aulaRepository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        aulaRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/aulas/{id}")
    public ResponseEntity<Aula> updateById(@PathVariable Long id, @RequestBody Aula aula){
        if(!aulaRepository.existsById(id)){
            return ResponseEntity.notFound().build();
        }
        aula.setId(id);
        return ResponseEntity.ok(aulaRepository.save(aula));
    }

}
