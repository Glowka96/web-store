package com.example.porfolio.webstorespring.services.orders;

import com.example.porfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.porfolio.webstorespring.mappers.ProducerMapper;
import com.example.porfolio.webstorespring.mappers.ProductMapper;
import com.example.porfolio.webstorespring.mappers.ShipmentMapper;
import com.example.porfolio.webstorespring.model.dto.orders.ShipmentRequest;
import com.example.porfolio.webstorespring.model.dto.orders.ShipmentResponse;
import com.example.porfolio.webstorespring.model.dto.products.ProductRequest;
import com.example.porfolio.webstorespring.model.entity.orders.Shipment;
import com.example.porfolio.webstorespring.model.entity.products.Product;
import com.example.porfolio.webstorespring.repositories.orders.ShipmentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
class ShipmentServiceTest {

    @Mock
    private ShipmentRepository shipmentRepository;
    @Spy
    private ShipmentMapper shipmentMapper = Mappers.getMapper(ShipmentMapper.class);
    @InjectMocks
    private ShipmentService underTest;
    private Shipment shipment;
    private ShipmentRequest shipmentRequest;

    @BeforeEach
    void initialization() {
        ProductMapper productMapper = Mappers.getMapper(ProductMapper.class);
        ReflectionTestUtils.setField(shipmentMapper, "productMapper", productMapper);

        ProducerMapper producerMapper = Mappers.getMapper(ProducerMapper.class);
        ReflectionTestUtils.setField(productMapper, "producerMapper", producerMapper);

        Product product = new Product();
        product.setId(1L);
        product.setPrice(20.0);

        ProductRequest productRequest = new ProductRequest();
        productRequest.setPrice(20.0);

        shipment = new Shipment();
        shipment.setId(1L);
        shipment.setProduct(product);
        shipment.setQuantity(3);

        shipmentRequest = new ShipmentRequest();
        shipmentRequest.setProductRequest(productRequest);
        shipmentRequest.setQuantity(3);
    }

    @Test
    void shouldGetShipmentDtoById() {
        // given
        given(shipmentRepository.findById(anyLong())).willReturn(Optional.of(shipment));

        // when
        ShipmentResponse savedShipmentResponse = underTest.getShipmentDtoById(1L);

        // then
        assertThat(savedShipmentResponse).isNotNull();
        assertThat(savedShipmentResponse.getId()).isEqualTo(shipment.getId());
    }

    @Test
    void willThrowWhenShipmentByIdIsNotFound() {
        // given
        given(shipmentRepository.findById(anyLong())).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.getShipmentDtoById(1L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Shipment with id 1 not found");
    }

    @Test
    void shouldGetAllShipment() {
        // given
        // when
        underTest.getAllShipment();

        // then
        verify(shipmentRepository, times(1)).findAll();
        verifyNoMoreInteractions(shipmentRepository);
    }

    @Test
    void shouldSaveShipment() {
        // given
        // when
        ShipmentResponse savedShipmentResponse = underTest.save(shipmentRequest);

        //then
        ArgumentCaptor<Shipment> shipmentArgumentCaptor =
                ArgumentCaptor.forClass(Shipment.class);
        verify(shipmentRepository).save(shipmentArgumentCaptor.capture());

        Shipment capturedShipment = shipmentArgumentCaptor.getValue();
        ShipmentResponse mappedShipmentResponse = shipmentMapper.mapToDto(capturedShipment);

        assertThat(mappedShipmentResponse.getId()).isEqualTo(savedShipmentResponse.getId());
        assertThat(mappedShipmentResponse.getProductResponse()).isEqualTo(savedShipmentResponse.getProductResponse());
        assertThat(mappedShipmentResponse.getQuantity()).isEqualTo(savedShipmentResponse.getQuantity());
    }

    @Test
    void shouldUpdateShipment() {
        // given
        given(shipmentRepository.findById(anyLong())).willReturn(Optional.of(shipment));

        // when
        ShipmentResponse savedShipmentResponse = underTest.update(1L, shipmentRequest);

        // then
        ArgumentCaptor<Shipment> shipmentArgumentCaptor =
                ArgumentCaptor.forClass(Shipment.class);
        verify(shipmentRepository).save(shipmentArgumentCaptor.capture());

        Shipment capturedShipment = shipmentArgumentCaptor.getValue();
        ShipmentResponse mappedShipmentResponse = shipmentMapper.mapToDto(capturedShipment);

        assertThat(mappedShipmentResponse).isEqualTo(savedShipmentResponse);
    }

    @Test
    void shouldDeleteById() {
        // given
        given(shipmentRepository.findById(anyLong())).willReturn(Optional.of(shipment));

        // when
        underTest.deleteShipmentById(1L);

        // then
        verify(shipmentRepository, times(1)).findById(1L);
        verify(shipmentRepository, times(1)).delete(shipment);
    }
}