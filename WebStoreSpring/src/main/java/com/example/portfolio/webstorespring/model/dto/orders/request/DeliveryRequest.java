package com.example.portfolio.webstorespring.model.dto.orders.request;

import com.example.portfolio.webstorespring.model.dto.AddressRegex;
import jakarta.validation.constraints.Pattern;
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

    private Long deliveryTypeId;
}
