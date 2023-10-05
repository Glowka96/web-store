package com.example.portfolio.webstorespring.model.dto.products;

import com.example.portfolio.webstorespring.model.entity.products.ProductType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Digits(integer = 8, fraction = 2, message = "The price is invalid")
    private Double price;

    private ProductType type;
}
