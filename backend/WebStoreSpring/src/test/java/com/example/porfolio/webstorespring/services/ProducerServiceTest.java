package com.example.porfolio.webstorespring.services;

import com.example.porfolio.webstorespring.exceptions.ResourceNotFoundException;
import com.example.porfolio.webstorespring.mappers.ProducerMapper;
import com.example.porfolio.webstorespring.model.dto.products.ProducerDto;
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

    private ProducerDto producerDto;
    private Producer producer;

    @BeforeEach
    void initialization() {
        producer = new Producer();
        producer.setId(1L);
        producer.setName("ProducerTest");

        producerDto = new ProducerDto();
        producerDto.setId(3L);
        producerDto.setName("Test");
    }

    @Test
    void shouldGetProducerById() {
        // given
        given(producerRepository.findById(producer.getId())).willReturn(Optional.of(producer));

        // when
        producerDto = underTest.getProducerById(1L);

        // then
        assertThat(producerDto).isNotNull();
        assertThat(producerDto.getName()).isNotNull();
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
        underTest.save(producerDto);

        // then
        ArgumentCaptor<Producer> producerArgumentCaptor =
                ArgumentCaptor.forClass(Producer.class);
        verify(producerRepository).save(producerArgumentCaptor.capture());

        Producer captureProducer = producerArgumentCaptor.getValue();
        ProducerDto mappedProducerDto = producerMapper.mapToDto(captureProducer);

        assertThat(mappedProducerDto).isEqualTo(producerDto);
    }

    @Test
    void shouldUpdateProducer() {
        // given
        given(producerRepository.findById(producer.getId())).willReturn(Optional.of(producer));

        // when
        ProducerDto savedProducer = underTest.update(producer.getId(), producerDto);

        // then
        assertThat(savedProducer.getName()).isEqualTo(producerDto.getName());
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