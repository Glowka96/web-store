package com.example.portfolio.webstorespring.model.dto.orders.request;

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

    private static final String DELIVERY_ADDRESS_PATTERN =
            "^[A-Za-z]+(, )\\d{2}-\\d{3}(, )(ul(.)?\\s)?[A-Z]?[a-z]+\\s\\d{1,3}((/\\d{1,3})?|(\\sm(.)?\\s)\\d{1,3})[a-z]?$";

    @Pattern(regexp = DELIVERY_ADDRESS_PATTERN,
            message = "The delivery address format is invalid")
    private String deliveryAddress;

    @Valid
    @JsonProperty(value = "deliveryType")
    private DeliveryTypeRequest deliveryTypeRequest;
}
