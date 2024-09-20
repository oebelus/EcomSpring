package com.oebelus.shop.service.cart;

import com.oebelus.shop.model.Cart;
import com.oebelus.shop.model.User;

import java.math.BigDecimal;

public interface ICartService {

    Cart getCart(Long id);

    void clearCart(Long id);

    BigDecimal getTotalPrice(Long id);

    Cart initializeNewCart(User user);

    Cart getCartByUserId(Long userId);
}
