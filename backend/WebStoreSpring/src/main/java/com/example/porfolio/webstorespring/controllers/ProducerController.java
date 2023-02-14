package com.example.porfolio.webstorespring.controllers;

import com.example.porfolio.webstorespring.model.dto.products.ProducerDto;
import com.example.porfolio.webstorespring.services.ProducerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("api/v1/producers")
@RequiredArgsConstructor
public class ProducerController {

    private final ProducerService producerService;

    @GetMapping()
    public ResponseEntity<List<ProducerDto>> getAllProducer() {
        return ResponseEntity.ok(producerService.getAllProducer());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProducerDto> getProducerById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(producerService.getProducerById(id));
    }

    @PostMapping
    public ResponseEntity<ProducerDto> saveProducer(@Valid @RequestBody ProducerDto producerDto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(producerService.save(producerDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProducerDto> updateProducer(@PathVariable("id") Long id,
                                                      @Valid @RequestBody ProducerDto producerDto) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(producerService.update(id, producerDto));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProducer(@PathVariable("id") Long id) {
        producerService.deleteById(id);
    }
}
