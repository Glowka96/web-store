package com.example.portfolio.webstorespring.services.orders;

import com.example.portfolio.webstorespring.mappers.DeliveryTypeMapper;
import com.example.portfolio.webstorespring.model.dto.orders.request.DeliveryTypeRequest;
import com.example.portfolio.webstorespring.model.dto.orders.response.DeliveryTypeResponse;
import com.example.portfolio.webstorespring.model.entity.orders.DeliveryType;
import com.example.portfolio.webstorespring.repositories.orders.DeliveryTypeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.example.portfolio.webstorespring.buildhelpers.orders.DeliveryTypeBuilderHelper.createDeliveryTypeRequest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeliveryTypeServiceTest {

    @Mock
    private DeliveryTypeRepository deliveryTypeRepository;
    @Spy
    private DeliveryTypeMapper deliveryTypeMapper;
    @InjectMocks
    private DeliveryTypeService underTest;

    @Test
    void shouldGetAllDeliveryType() {
        underTest.getAll();

        verify(deliveryTypeRepository, times(1)).findAll();
        verifyNoMoreInteractions(deliveryTypeRepository);
        verify(deliveryTypeMapper, times(1)).mapToDto(anyList());
        verifyNoMoreInteractions(deliveryTypeMapper);
    }

    @Test
    void shouldSaveDeliveryType() {
        DeliveryTypeRequest deliveryTypeRequest = createDeliveryTypeRequest();

        DeliveryTypeResponse savedDeliveryTypeResponse = underTest.save(deliveryTypeRequest);

        ArgumentCaptor<DeliveryType> deliveryTypeArgumentCaptor =
                ArgumentCaptor.forClass(DeliveryType.class);
        verify(deliveryTypeRepository).save(deliveryTypeArgumentCaptor.capture());

        DeliveryTypeResponse mappedDeliveryTypeResponse =
                deliveryTypeMapper.mapToDto(deliveryTypeArgumentCaptor.getValue());

        assertEquals(mappedDeliveryTypeResponse, savedDeliveryTypeResponse);
    }

    @Test
    void shouldDeleteDeliveryType() {
        underTest.deleteById(anyLong());

        verify(deliveryTypeRepository, times(1)).deleteById(anyLong());
        verifyNoMoreInteractions(deliveryTypeRepository);
    }
}
