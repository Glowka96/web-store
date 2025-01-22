package com.example.portfolio.webstorespring.repositories.tokens.removals;

import com.example.portfolio.webstorespring.model.entity.tokens.removals.SingleProductRemovalToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SingleProductRemovalRepository extends JpaRepository<SingleProductRemovalToken, Long> {
}
