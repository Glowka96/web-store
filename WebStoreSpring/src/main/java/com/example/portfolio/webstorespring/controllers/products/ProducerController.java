package com.example.portfolio.webstorespring.controllers.products;

import com.example.portfolio.webstorespring.models.dtos.products.requests.ProducerRequest;
import com.example.portfolio.webstorespring.models.dtos.products.responses.ProducerResponse;
import com.example.portfolio.webstorespring.services.products.ProducerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/admin/producers")
@RequiredArgsConstructor
public class ProducerController {

    private final ProducerService producerService;

    @GetMapping
    public List<ProducerResponse> getAllProducer() {
        return producerService.getAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProducerResponse saveProducer(@Valid @RequestBody ProducerRequest request) {
        return producerService.save(request);
    }

    @PutMapping("/{id}")
    public ProducerResponse updateProducer(@PathVariable("id") Long id,
                                           @Valid @RequestBody ProducerRequest request) {
        return producerService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProducer(@PathVariable("id") Long id) {
        producerService.deleteById(id);
    }
}
