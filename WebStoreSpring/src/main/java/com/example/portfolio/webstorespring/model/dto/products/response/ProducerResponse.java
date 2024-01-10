package com.example.portfolio.webstorespring.model.dto.products.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProducerResponse {

    private Long id;

    private String name;
}
