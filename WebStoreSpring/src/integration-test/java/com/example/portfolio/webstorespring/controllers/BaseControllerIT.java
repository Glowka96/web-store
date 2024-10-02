package com.example.portfolio.webstorespring.controllers;

import org.springframework.core.ParameterizedTypeReference;

import java.util.List;
import java.util.Optional;

public interface BaseControllerIT<T, R, E> {

    String getUri();
    T createRequest();
    Class<R> getResponseTypeClass();
    ParameterizedTypeReference<List<R>> getListResponseTypeClass();
    List<E> getAllEntities();
    Optional<E> getOptionalEntityBySavedId();

}
