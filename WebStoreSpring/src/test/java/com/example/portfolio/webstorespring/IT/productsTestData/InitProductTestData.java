package com.example.portfolio.webstorespring.IT.productsTestData;

import com.example.portfolio.webstorespring.IT.controllers.InitTestData;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface InitProductTestData extends InitTestData {
    void initOneProduct();
    Long getProductIdWhatHasPromotion();
    Long getProductIdWhatNotHasPromotion();
    Long getSubId();
    Long getProducerId();
    LocalDateTime getDate30DaysAgo();

    Pageable getPageable();
}
