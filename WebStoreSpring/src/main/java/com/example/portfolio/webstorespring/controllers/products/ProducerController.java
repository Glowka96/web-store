package com.example.portfolio.webstorespring.controllers.products;

import com.example.portfolio.webstorespring.model.dto.products.ProducerRequest;
import com.example.portfolio.webstorespring.model.dto.products.ProducerResponse;
import com.example.portfolio.webstorespring.services.products.ProducerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@CrossOrigin("*")
@RequestMapping(value = "api/v1/producers")
@RequiredArgsConstructor
public class ProducerController {

    private final ProducerService producerService;

    @GetMapping()
    public ResponseEntity<List<ProducerResponse>> getAllProducer() {
        return ResponseEntity.ok(producerService.getAllProducer());
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ProducerResponse> getProducerById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(producerService.getProducerById(id));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping()
    public ResponseEntity<ProducerResponse> saveProducer(@Valid @RequestBody ProducerRequest producerRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(producerService.save(producerRequest));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping(value = "/{id}")
    public ResponseEntity<ProducerResponse> updateProducer(@PathVariable("id") Long id,
                                                           @Valid @RequestBody ProducerRequest producerRequest) {
        return ResponseEntity.status(HttpStatus.ACCEPTED)
                .body(producerService.update(id, producerRequest));
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProducer(@PathVariable("id") Long id) {
        producerService.deleteById(id);
    }
}
