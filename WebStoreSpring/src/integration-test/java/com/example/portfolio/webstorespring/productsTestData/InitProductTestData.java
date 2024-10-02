package com.example.portfolio.webstorespring.productsTestData;

import com.example.portfolio.webstorespring.controllers.InitTestData;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface InitProductTestData extends InitTestData {
    void initOneProduct();
    Long getProductIdThatHasPromotion();
    Long getProductIdThatHasNoPromotion();
    Long getSubId();
    Long getProducerId();
    Long getProductTypeId();
    LocalDateTime getDate30DaysAgo();

    Pageable getPageable();
}
