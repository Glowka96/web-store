package com.example.portfolio.webstorespring.model.dto.products.request;

import com.example.portfolio.webstorespring.enums.ProductType;
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

    private Long id;

    @NotNull(message = "The name can't be null")
    @NotBlank(message = "The name can't be blank")
    @Size(min = 3, max = 32, message = "The name must between min 3 and max 32 letters")
    private String name;

    @NotNull(message = "The description can't be null")
    @NotBlank(message = "The description can't be blank")
    @Size(min = 3, max = 512, message = "The description must between min 3 and max 512 letters")
    private String description;

    @NotNull(message = "The image url can't be null")
    @NotBlank(message = "The image url can't be blank")
    @Pattern(regexp = "^(http(s)?:)?([/|.|\\w|\\s|-])*\\.(?:jpg|png)$",
            message = "This is not image url")
    private String imageUrl;

    @DecimalMin(value = "0.01", message = "The price must be greater than or equal to 0.01")
    @DecimalMax(value = "99999.99", message = "The price must be less than or equal to 99999999.99")
    private BigDecimal price;

    @Min(value = 1, message = "The quantity must be greater than or equal to one")
    @Max(value = 10_000, message = "The quantity must be less than or equal to one hundred")
    private Long quantity;

    private ProductType type;
}
