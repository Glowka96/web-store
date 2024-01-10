package com.example.portfolio.webstorespring.model.dto.products.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProducerRequest {

    @NotBlank(message = "The producer name can't be blank")
    @Size(min = 3, max = 20, message = "The producer name must between min 3 and max 20 letters")
    private String name;
}
