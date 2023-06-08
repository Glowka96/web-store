package com.example.porfolio.webstorespring.model.dto.orders;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.List;

@Data
public class OrderRequest {
    private final static String DELIVERY_ADDRESS_PATTERN =
            "^[A-Za-z]+(, )\\d{2}-\\d{3}(, )(ul(.)?\\s)?[A-Z]?[a-z]+\\s\\d{1,3}((/\\d{1,3})?|(\\sm(.)?\\s)\\d{1,3})[a-z]?$";

    @NotNull(message = "The delivery address can't be null")
    @NotBlank(message = "The delivery address can't be blank")
    @Pattern(regexp = DELIVERY_ADDRESS_PATTERN,
            message = "The delivery address format is invalid")
    private String deliveryAddress;

    @JsonProperty("shipments")
    private List<ShipmentRequest> shipmentRequests;
}
