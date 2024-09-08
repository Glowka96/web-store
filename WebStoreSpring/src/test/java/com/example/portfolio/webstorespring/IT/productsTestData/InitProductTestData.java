package com.example.portfolio.webstorespring.IT.productsTestData;

import com.example.portfolio.webstorespring.IT.controllers.InitTestData;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface InitProductTestData extends InitTestData {
    Long getProductId();
    Long getSubId();

    LocalDateTime getDate30DaysAgo();

    Pageable getPageable();
}
