package com.example.portfolio.webstorespring.services.orders;

import com.example.portfolio.webstorespring.exceptions.ResourceNotFoundException;
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

import java.util.Optional;

import static com.example.portfolio.webstorespring.buildhelpers.DeliveryTypeBuilderHelper.createDeliveryType;
import static com.example.portfolio.webstorespring.buildhelpers.DeliveryTypeBuilderHelper.createDeliveryTypeRequest;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
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
        // given
        // when
        underTest.getAllDeliveryType();

        // then
        verify(deliveryTypeRepository, times(1)).findAll();
        verifyNoMoreInteractions(deliveryTypeRepository);
        verify(deliveryTypeMapper, times(1)).mapToDto(anyList());
        verifyNoMoreInteractions(deliveryTypeMapper);
    }

    @Test
    void shouldSaveDeliveryType() {
        // given
        DeliveryTypeRequest deliveryTypeRequest = createDeliveryTypeRequest();

        // when
        DeliveryTypeResponse savedDeliveryTypeResponse = underTest.saveDeliveryType(deliveryTypeRequest);

        // then
        ArgumentCaptor<DeliveryType> deliveryTypeArgumentCaptor =
                ArgumentCaptor.forClass(DeliveryType.class);
        verify(deliveryTypeRepository).save(deliveryTypeArgumentCaptor.capture());

        DeliveryTypeResponse mappedDeliveryTypeResponse =
                deliveryTypeMapper.mapToDto(deliveryTypeArgumentCaptor.getValue());

        assertThat(savedDeliveryTypeResponse).isEqualTo(mappedDeliveryTypeResponse);
    }

    @Test
    void shouldDeleteDeliveryType() {
        // given
        DeliveryType deliveryType = createDeliveryType();
        given(deliveryTypeRepository.findById(anyLong())).willReturn(Optional.of(deliveryType));

        // when
        underTest.deleteDeliveryType(1L);

        // then
        verify(deliveryTypeRepository, times(1)).findById(1L);
        verify(deliveryTypeRepository, times(1)).delete(deliveryType);
    }

    @Test
    void willThrowWhenDeliveryTypeIdNotFound() {
        // given
        given(deliveryTypeRepository.findById(anyLong())).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.deleteDeliveryType(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                        .hasMessageContaining("DeliveryType with id 1 not found");
    }
}
