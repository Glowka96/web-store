package com.example.portfolio.webstorespring.services.products;

import com.example.portfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.portfolio.webstorespring.mappers.ProducerMapper;
import com.example.portfolio.webstorespring.model.dto.products.request.ProducerRequest;
import com.example.portfolio.webstorespring.model.dto.products.response.ProducerResponse;
import com.example.portfolio.webstorespring.model.entity.products.Producer;
import com.example.portfolio.webstorespring.repositories.products.ProducerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

    private ProducerRequest producerRequest;
    private Producer producer;

    @BeforeEach
    void initialization() {
        producer = new Producer();
        producer.setId(1L);
        producer.setName("ProducerTest");

        producerRequest = new ProducerRequest();
        producerRequest.setName("Test");
    }

    @Test
    void shouldGetProducerById() {
        // given
        given(producerRepository.findById(anyLong())).willReturn(Optional.of(producer));

        // when
        ProducerResponse foundProducerResponse = underTest.getProducerById(1L);

        // then
        assertThat(foundProducerResponse.getId()).isEqualTo(1L);
        assertThat(foundProducerResponse).isNotNull();
        assertThat(foundProducerResponse.getName()).isNotNull();
    }

    @Test
    void willThrowWhenProducerIdNotFound() {
        // given
        given(producerRepository.findById(2L)).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> underTest.getProducerById(2L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("Producer with id 2 not found");
    }

    @Test
    void shouldGetAllProducer() {
        // given
        // when
        underTest.getAllProducer();

        // then
        verify(producerRepository, times(1)).findAll();
        verifyNoMoreInteractions(producerRepository);
    }

    @Test
    void shouldSaveProducer() {
        // given
        // when
        ProducerResponse savedProducerResponse = underTest.saveProducer(producerRequest);

        // then
        ArgumentCaptor<Producer> producerArgumentCaptor =
                ArgumentCaptor.forClass(Producer.class);
        verify(producerRepository).save(producerArgumentCaptor.capture());

        Producer captureProducer = producerArgumentCaptor.getValue();
        ProducerResponse mappedProducerResponse = producerMapper.mapToDto(captureProducer);

        assertThat(mappedProducerResponse).isEqualTo(savedProducerResponse);
    }

    @Test
    void shouldUpdateProducer() {
        // given
        given(producerRepository.findById(producer.getId())).willReturn(Optional.of(producer));

        // when
        ProducerResponse updatedProducer = underTest.updateProducer(producer.getId(), producerRequest);

        // then
        assertThat(updatedProducer.getName()).isEqualTo(producerRequest.getName());
        assertThat(updatedProducer.getId()).isEqualTo(producer.getId());
    }

    @Test
    void shouldDeleteProducerById() {
        // given
        given(producerRepository.findById(producer.getId())).willReturn(Optional.of(producer));

        // when
        underTest.deleteById(producer.getId());

        // then
        verify(producerRepository, times(1)).findById(producer.getId());
        verify(producerRepository, times(1)).delete(producer);
        verifyNoMoreInteractions(producerRepository);
    }
}
