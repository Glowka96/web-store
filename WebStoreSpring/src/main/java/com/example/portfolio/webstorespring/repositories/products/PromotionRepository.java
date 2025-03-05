package com.example.portfolio.webstorespring.repositories.products;

import com.example.portfolio.webstorespring.models.entity.products.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromotionRepository extends JpaRepository<Promotion, Long> {
}
