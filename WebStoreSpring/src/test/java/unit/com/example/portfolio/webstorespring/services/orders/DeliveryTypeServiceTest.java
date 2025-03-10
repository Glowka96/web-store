package com.example.portfolio.webstorespring.services.orders;

import com.example.portfolio.webstorespring.mappers.DeliveryTypeMapper;
import com.example.portfolio.webstorespring.models.dtos.orders.requests.DeliveryTypeRequest;
import com.example.portfolio.webstorespring.models.dtos.orders.responses.DeliveryTypeResponse;
import com.example.portfolio.webstorespring.models.entities.orders.DeliveryType;
import com.example.portfolio.webstorespring.repositories.orders.DeliveryTypeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static com.example.portfolio.webstorespring.buildhelpers.orders.DeliveryTypeBuilderHelper.createDeliveryType;
import static com.example.portfolio.webstorespring.buildhelpers.orders.DeliveryTypeBuilderHelper.createDeliveryTypeRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeliveryTypeServiceTest {

    @Mock
    private DeliveryTypeRepository deliveryTypeRepository;
    @InjectMocks
    private DeliveryTypeService underTest;

    @Test
    void shouldGetAllDeliveryType() {
        underTest.getAll();

        verify(deliveryTypeRepository, times(1)).findAll();
        verifyNoMoreInteractions(deliveryTypeRepository);
    }

    @Test
    void shouldSaveDeliveryType() {
        DeliveryTypeRequest deliveryTypeRequest = createDeliveryTypeRequest();

        DeliveryTypeResponse savedDeliveryTypeResponse = underTest.save(deliveryTypeRequest);

        ArgumentCaptor<DeliveryType> deliveryTypeArgumentCaptor =
                ArgumentCaptor.forClass(DeliveryType.class);
        verify(deliveryTypeRepository).save(deliveryTypeArgumentCaptor.capture());

        DeliveryTypeResponse mappedDeliveryTypeResponse =
                DeliveryTypeMapper.mapToResponse(deliveryTypeArgumentCaptor.getValue());

        assertEquals(mappedDeliveryTypeResponse, savedDeliveryTypeResponse);
    }

    @Test
    void shouldUpdateDeliveryType() {
        DeliveryType deliveryType = createDeliveryType();
        String nameBeforeUpdate = deliveryType.getName();
        BigDecimal priceBeforeUpdate = deliveryType.getPrice();
        DeliveryTypeRequest deliveryTypeRequest = createDeliveryTypeRequest("update", BigDecimal.valueOf(9));

        given(deliveryTypeRepository.findById(anyLong())).willReturn(Optional.of(deliveryType));

        DeliveryTypeResponse updateDeliveryTypeResponse = underTest.update(1L, deliveryTypeRequest);

        assertNotEquals(nameBeforeUpdate, updateDeliveryTypeResponse.name());
        assertNotEquals(priceBeforeUpdate, updateDeliveryTypeResponse.price());
    }

    @Test
    void shouldDeleteDeliveryType() {
        underTest.deleteById(anyLong());

        verify(deliveryTypeRepository, times(1)).deleteById(anyLong());
        verifyNoMoreInteractions(deliveryTypeRepository);
    }
}
