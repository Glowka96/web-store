package com.example.portfolio.webstorespring.model.dto.orders.request;

import com.example.portfolio.webstorespring.model.dto.StreetRegex;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
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

    @Pattern(regexp = StreetRegex.PATTERN,
            message = "The delivery address format is invalid")
    private String deliveryAddress;

    @Valid
    @JsonProperty(value = "deliveryType")
    private DeliveryTypeRequest deliveryTypeRequest;
}
