package com.example.porfolio.webstorespring.services;

import com.example.porfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.porfolio.webstorespring.mappers.ProducerMapper;
import com.example.porfolio.webstorespring.model.dto.products.ProducerResponse;
import com.example.porfolio.webstorespring.model.entity.products.Producer;
import com.example.porfolio.webstorespring.repositories.products.ProducerRepository;
import com.example.porfolio.webstorespring.services.products.ProducerService;
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

    private ProducerResponse producerResponse;
    private Producer producer;

    @BeforeEach
    void initialization() {
        producer = new Producer();
        producer.setId(1L);
        producer.setName("ProducerTest");

        producerResponse = new ProducerResponse();
        producerResponse.setId(3L);
        producerResponse.setName("Test");
    }

    @Test
    void shouldGetProducerById() {
        // given
        given(producerRepository.findById(producer.getId())).willReturn(Optional.of(producer));

        // when
        producerResponse = underTest.getProducerById(1L);

        // then
        assertThat(producerResponse).isNotNull();
        assertThat(producerResponse.getName()).isNotNull();
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
        underTest.save(producerResponse);

        // then
        ArgumentCaptor<Producer> producerArgumentCaptor =
                ArgumentCaptor.forClass(Producer.class);
        verify(producerRepository).save(producerArgumentCaptor.capture());

        Producer captureProducer = producerArgumentCaptor.getValue();
        ProducerResponse mappedProducerResponse = producerMapper.mapToDto(captureProducer);

        assertThat(mappedProducerResponse).isEqualTo(producerResponse);
    }

    @Test
    void shouldUpdateProducer() {
        // given
        given(producerRepository.findById(producer.getId())).willReturn(Optional.of(producer));

        // when
        ProducerResponse savedProducer = underTest.update(producer.getId(), producerResponse);

        // then
        assertThat(savedProducer.getName()).isEqualTo(producerResponse.getName());
        assertThat(savedProducer.getId()).isEqualTo(producer.getId());
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