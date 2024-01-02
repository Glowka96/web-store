package com.example.portfolio.webstorespring.controllers.products;

import com.example.portfolio.webstorespring.model.dto.products.request.ProducerRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.ProducerResponse;
import com.example.portfolio.webstorespring.services.products.ProducerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "api/v1/admin/producers")
@RequiredArgsConstructor
public class ProducerController {

    private final ProducerService producerService;

    @GetMapping()
    public ResponseEntity<List<ProducerResponse>> getAllProducer() {
        return ResponseEntity.ok(producerService.getAllProducer());
    }

    @PostMapping()
    public ResponseEntity<ProducerResponse> saveProducer(@Valid @RequestBody ProducerRequest producerRequest) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(producerService.saveProducer(producerRequest));
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ProducerResponse> updateProducer(@PathVariable("id") Long id,
                                                           @Valid @RequestBody ProducerRequest producerRequest) {
        return ResponseEntity.accepted()
                .body(producerService.updateProducer(id, producerRequest));
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteProducer(@PathVariable("id") Long id) {
        producerService.deleteProducerById(id);
        return ResponseEntity.noContent().build();

    }
}
