package com.example.portfolio.webstorespring.IT.controllers;

import java.util.List;

public interface AssertsFieldsIT<T, R, E> {
    void assertsFieldsWhenGetAll(List<R> responses);
    void assertsFieldsWhenSave(T request, R response);

    void assertsFieldsWhenUpdate(T request, R response, E entityBeforeUpdate, E entityAfterUpdate);

    void assertsFieldsWhenNotUpdate(T request, E entity);
}
