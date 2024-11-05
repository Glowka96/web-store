package com.example.portfolio.webstorespring.services.products;

import com.example.portfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.portfolio.webstorespring.mappers.ProducerMapper;
import com.example.portfolio.webstorespring.model.dto.products.request.ProducerRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.ProducerResponse;
import com.example.portfolio.webstorespring.model.entity.products.Producer;
import com.example.portfolio.webstorespring.repositories.products.ProducerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static com.example.portfolio.webstorespring.buildhelpers.products.ProducerBuilderHelper.createProducer;
import static com.example.portfolio.webstorespring.buildhelpers.products.ProducerBuilderHelper.createProducerRequest;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
class ProducerServiceTest {
    @Spy
    private ProducerMapper producerMapper = Mappers.getMapper(ProducerMapper.class);
    @Mock
    private ProducerRepository producerRepository;
    @InjectMocks
    private ProducerService underTest;

    @Test
    void shouldGetProducerById() {
        Producer producer = createProducer();
        given(producerRepository.findById(anyLong())).willReturn(Optional.of(producer));

        ProducerResponse foundProducerResponse = underTest.getProducerById(1L);

        assertEquals(1L, foundProducerResponse.getId());
        assertNotNull(foundProducerResponse);
        assertEquals(producer.getName(), foundProducerResponse.getName());
    }

    @Test
    void willThrowResourceNotFound_whenProducerIdNotFound() {
        given(producerRepository.findById(2L)).willReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> underTest.getProducerById(2L));


        assertThatThrownBy(() -> underTest.getProducerById(2L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Producer with id 2 not found");
    }

    @Test
    void shouldGetAllProducer() {
        underTest.getAllProducer();

        verify(producerRepository, times(1)).findAll();
        verifyNoMoreInteractions(producerRepository);
    }

    @Test
    void shouldSaveProducer() {
        ProducerRequest producerRequest = createProducerRequest();

        ProducerResponse savedProducerResponse = underTest.saveProducer(producerRequest);

        ArgumentCaptor<Producer> producerArgumentCaptor =
                ArgumentCaptor.forClass(Producer.class);
        verify(producerRepository).save(producerArgumentCaptor.capture());

        ProducerResponse mappedProducerResponse =
                producerMapper.mapToDto(producerArgumentCaptor.getValue());

        assertEquals(mappedProducerResponse, savedProducerResponse);
    }

    @Test
    void shouldUpdateProducer() {
        Producer producer = createProducer();
        String producerNameBeforeUpdate = producer.getName();
        ProducerRequest producerRequest = createProducerRequest("Test2");
        given(producerRepository.findById(anyLong())).willReturn(Optional.of(producer));

        ProducerResponse updatedProducerResponse = underTest.updateProducer(producer.getId(), producerRequest);

        ArgumentCaptor<Producer> producerArgumentCaptor =
                ArgumentCaptor.forClass(Producer.class);
        verify(producerRepository).save(producerArgumentCaptor.capture());

        ProducerResponse mappedCategoryResponse = producerMapper.mapToDto(producerArgumentCaptor.getValue());

        assertEquals(mappedCategoryResponse, updatedProducerResponse);
        assertNotEquals(producerNameBeforeUpdate, mappedCategoryResponse.getName());
    }

    @Test
    void shouldDeleteProducerById() {
        underTest.deleteProducerById(anyLong());

        verify(producerRepository, times(1)).deleteById(anyLong());
        verifyNoMoreInteractions(producerRepository);
    }
}
