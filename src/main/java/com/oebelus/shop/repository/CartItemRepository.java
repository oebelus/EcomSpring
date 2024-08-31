package com.oebelus.shop.repository;

import com.oebelus.shop.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    void deleteAllByCartId(Long id);

    void deleteByCartIdAndProductId(Long cartId, Long productId);
}
