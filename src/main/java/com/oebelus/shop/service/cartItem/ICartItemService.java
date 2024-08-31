package com.oebelus.shop.service.cartItem;

import com.oebelus.shop.model.CartItem;

public interface ICartItemService {

    void addItem(Long cartId, Long productId, int quantity);

    void updateQuantity(Long cartId, Long productId, int quantity);

    void deleteItem(Long cartId, Long productId);

    CartItem getCartItem(Long cartId, Long productId);
}
