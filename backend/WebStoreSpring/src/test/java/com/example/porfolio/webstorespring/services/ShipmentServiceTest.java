package com.example.porfolio.webstorespring.services;

import com.example.porfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.porfolio.webstorespring.mappers.ProducerMapper;
import com.example.porfolio.webstorespring.mappers.ProductMapper;
import com.example.porfolio.webstorespring.mappers.ShipmentMapper;
import com.example.porfolio.webstorespring.model.dto.orders.ShipmentResponse;
import com.example.porfolio.webstorespring.model.dto.products.ProductRequest;
import com.example.porfolio.webstorespring.model.entity.orders.Shipment;
import com.example.porfolio.webstorespring.model.entity.products.Product;
import com.example.porfolio.webstorespring.repositories.orders.ShipmentRepository;
import com.example.porfolio.webstorespring.services.orders.ShipmentService;
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
    private ProductRequest productRequest;
    private Shipment shipment;
    private ShipmentResponse shipmentResponse;

    @BeforeEach
    void initialization() {
        ProductMapper productMapper = Mappers.getMapper(ProductMapper.class);
        ReflectionTestUtils.setField(shipmentMapper, "productMapper", productMapper);

        ProducerMapper producerMapper = Mappers.getMapper(ProducerMapper.class);
        ReflectionTestUtils.setField(productMapper, "producerMapper", producerMapper);

        Product product = new Product();
        product.setId(1L);
        product.setPrice(20.0);

        productRequest = new ProductRequest();
        productRequest.setId(1L);
        productRequest.setPrice(20.0);

        shipment = new Shipment();
        shipment.setId(1L);
        shipment.setProduct(product);
        shipment.setQuantity(3);

        shipmentResponse = new ShipmentResponse();
        shipmentResponse.setId(1L);
        shipmentResponse.setProductRequest(productRequest);
        shipmentResponse.setQuantity(3);
    }

    @Test
    void shouldGetShipmentDtoById() {
        // given
        given(shipmentRepository.findById(1L)).willReturn(Optional.of(shipment));

        // when
        shipmentResponse = underTest.getShipmentDtoById(1L);

        // then
        assertThat(shipment).isNotNull();
        assertThat(shipment.getId()).isEqualTo(shipment.getId());
    }

    @Test
    void willThrowWhenShipmentByIdIsNotFound() {
        // given
        given(shipmentRepository.findById(1L)).willReturn(Optional.empty());

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
        underTest.save(shipmentResponse);

        //then
        ArgumentCaptor<Shipment> shipmentArgumentCaptor =
                ArgumentCaptor.forClass(Shipment.class);
        verify(shipmentRepository).save(shipmentArgumentCaptor.capture());

        Shipment capturedShipment = shipmentArgumentCaptor.getValue();
        ShipmentResponse mappedShipmentResponse = shipmentMapper.mapToDto(capturedShipment);

        assertThat(mappedShipmentResponse.getId()).isEqualTo(shipmentResponse.getId());
        assertThat(mappedShipmentResponse.getProductRequest()).isEqualTo(productRequest);
        assertThat(mappedShipmentResponse.getQuantity()).isEqualTo(shipmentResponse.getQuantity());
    }

    @Test
    void shouldUpdateShipment() {
        // given
        given(shipmentRepository.findById(1L)).willReturn(Optional.of(shipment));

        // when
        underTest.update(1L, shipmentResponse);

        // then
        ArgumentCaptor<Shipment> shipmentArgumentCaptor =
                ArgumentCaptor.forClass(Shipment.class);
        verify(shipmentRepository).save(shipmentArgumentCaptor.capture());

        Shipment capturedShipment = shipmentArgumentCaptor.getValue();
        ShipmentResponse mappedShipmentResponse = shipmentMapper.mapToDto(capturedShipment);

        assertThat(mappedShipmentResponse).isEqualTo(shipmentResponse);
    }

    @Test
    void shouldDeleteById() {
        // given
        given(shipmentRepository.findById(1L)).willReturn(Optional.of(shipment));

        // when
        underTest.deleteShipmentById(1L);

        // then
        verify(shipmentRepository, times(1)).findById(1L);
        verify(shipmentRepository, times(1)).delete(shipment);
    }
}