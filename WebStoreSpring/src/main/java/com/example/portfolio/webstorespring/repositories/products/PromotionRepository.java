package com.example.portfolio.webstorespring.repositories.products;

import com.example.portfolio.webstorespring.models.entities.products.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PromotionRepository extends JpaRepository<Promotion, Long> {
}
