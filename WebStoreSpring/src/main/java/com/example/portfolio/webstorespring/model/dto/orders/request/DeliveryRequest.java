package com.example.portfolio.webstorespring.model.dto.orders.request;

import com.example.portfolio.webstorespring.model.dto.AddressRegex;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryRequest {

    @Pattern(regexp = AddressRegex.COMPLETE_ADDRESS,
            message = "The delivery address format is invalid")
    private String deliveryAddress;

    @Positive(message = "The delivery type id must be positive number")
    @NotNull(message = "The delivery type id can't be null")
    private Long deliveryTypeId;
}
