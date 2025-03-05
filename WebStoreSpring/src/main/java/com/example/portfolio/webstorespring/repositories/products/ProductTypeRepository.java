package com.example.portfolio.webstorespring.repositories.products;

import com.example.portfolio.webstorespring.models.entity.products.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductTypeRepository extends JpaRepository<ProductType, Long> {
}
