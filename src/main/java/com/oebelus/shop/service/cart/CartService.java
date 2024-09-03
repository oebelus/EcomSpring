package com.oebelus.shop.service.cart;

import com.oebelus.shop.exceptions.ResourceNotFoundException;
import com.oebelus.shop.model.Cart;
import com.oebelus.shop.repository.CartItemRepository;
import com.oebelus.shop.repository.CartRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor // Constructor Injection
public class CartService implements ICartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final AtomicLong cartIdGenerator = new AtomicLong(0);

    @Override
    public Cart getCart(Long id) {
        Cart cart = cartRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Cart not found!"));
        BigDecimal totalPrice = cart.getTotalPrice();
        cart.setTotalPrice(totalPrice);

        return cartRepository.save(cart);
    }

    @Transactional
    @Override
    public void clearCart(Long id) {
        Cart cart = getCart(id);
        cartItemRepository.deleteAllByCartId(id);
        cart.getCartItems().clear();
        cartRepository.deleteById(id);
    }

    @Override
    public BigDecimal getTotalPrice(Long id) {
        return getCart(id).getTotalPrice();
    }

    @Override
    public Long initializeNewCart() {
        Cart newCart = new Cart();
        newCart.setId(cartIdGenerator.incrementAndGet());

        return cartRepository.save(newCart).getId();
    }

    @Override
    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId);
    }
}
