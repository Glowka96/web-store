package com.example.portfolio.webstorespring.services.products;

import com.example.portfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.portfolio.webstorespring.mappers.ProducerMapper;
import com.example.portfolio.webstorespring.models.dto.products.request.ProducerRequest;
import com.example.portfolio.webstorespring.models.dto.products.response.ProducerResponse;
import com.example.portfolio.webstorespring.models.entity.products.Producer;
import com.example.portfolio.webstorespring.repositories.products.ProducerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
    @Mock
    private ProducerRepository producerRepository;
    @InjectMocks
    private ProducerService underTest;

    @Test
    void shouldGetProducerById() {
        Producer producer = createProducer();
        given(producerRepository.findById(anyLong())).willReturn(Optional.of(producer));

        ProducerResponse foundProducerResponse = underTest.getById(1L);

        assertEquals(1L, foundProducerResponse.id());
        assertNotNull(foundProducerResponse);
        assertEquals(producer.getName(), foundProducerResponse.name());
    }

    @Test
    void willThrowResourceNotFound_whenProducerIdNotFound() {
        given(producerRepository.findById(2L)).willReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> underTest.getById(2L));


        assertThatThrownBy(() -> underTest.getById(2L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Producer with id 2 not found");
    }

    @Test
    void shouldGetAllProducer() {
        underTest.getAll();

        verify(producerRepository, times(1)).findAll();
        verifyNoMoreInteractions(producerRepository);
    }

    @Test
    void shouldSaveProducer() {
        ProducerRequest producerRequest = createProducerRequest();

        ProducerResponse savedProducerResponse = underTest.save(producerRequest);

        ArgumentCaptor<Producer> producerArgumentCaptor =
                ArgumentCaptor.forClass(Producer.class);
        verify(producerRepository).save(producerArgumentCaptor.capture());

        ProducerResponse mappedProducerResponse =
                ProducerMapper.mapToDto(producerArgumentCaptor.getValue());

        assertEquals(mappedProducerResponse, savedProducerResponse);
    }

    @Test
    void shouldUpdateProducer() {
        Producer producer = createProducer();
        String producerNameBeforeUpdate = producer.getName();
        ProducerRequest producerRequest = createProducerRequest("Test2");
        given(producerRepository.findById(anyLong())).willReturn(Optional.of(producer));

        ProducerResponse updatedProducerResponse = underTest.update(producer.getId(), producerRequest);

        ArgumentCaptor<Producer> producerArgumentCaptor =
                ArgumentCaptor.forClass(Producer.class);
        verify(producerRepository).save(producerArgumentCaptor.capture());

        ProducerResponse mappedCategoryResponse = ProducerMapper.mapToDto(producerArgumentCaptor.getValue());

        assertEquals(mappedCategoryResponse, updatedProducerResponse);
        assertNotEquals(producerNameBeforeUpdate, mappedCategoryResponse.name());
    }

    @Test
    void shouldDeleteProducerById() {
        underTest.deleteById(anyLong());

        verify(producerRepository, times(1)).deleteById(anyLong());
        verifyNoMoreInteractions(producerRepository);
    }
}
