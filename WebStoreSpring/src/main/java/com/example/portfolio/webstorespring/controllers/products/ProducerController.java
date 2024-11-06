package com.example.portfolio.webstorespring.controllers.products;

import com.example.portfolio.webstorespring.model.dto.products.request.ProducerRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.ProducerResponse;
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
        return producerService.getAllProducer();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProducerResponse saveProducer(@Valid @RequestBody ProducerRequest producerRequest) {
        return producerService.saveProducer(producerRequest);
    }

    @PutMapping("/{id}")
    public ProducerResponse updateProducer(@PathVariable("id") Long id,
                                           @Valid @RequestBody ProducerRequest producerRequest) {
        return producerService.updateProducer(id, producerRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProducer(@PathVariable("id") Long id) {
        producerService.deleteProducerById(id);
    }
}
