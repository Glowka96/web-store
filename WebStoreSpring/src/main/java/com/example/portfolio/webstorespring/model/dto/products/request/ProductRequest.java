package com.example.portfolio.webstorespring.model.dto.products.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {

    @NotBlank(message = "The name can't be blank")
    @Size(min = 3, max = 32, message = "The name must between min 3 and max 32 letters")
    private String name;

    @NotBlank(message = "The description can't be blank")
    @Size(min = 3, max = 512, message = "The description must between min 3 and max 512 letters")
    private String description;

    @NotBlank(message = "The image url can't be blank")
    @Pattern(regexp = "^(http(s)?:)?([/|.|\\w|-])*\\.(?:jpg|png)$",
            message = "This is not image url")
    private String imageUrl;

    @DecimalMin(value = "0.01", message = "The price must be greater than or equal to 0.01")
    @DecimalMax(value = "99999.99", message = "The price must be less than or equal to 99999.99")
    private BigDecimal price;

    @Min(value = 1, message = "The quantity must be greater than or equal to one")
    @Max(value = 10_000, message = "The quantity must be less than or equal to 10000")
    private Long quantity;

    @Valid
    @NotNull(message = "The product type can't be null")
    private ProductTypeRequest type;
}
