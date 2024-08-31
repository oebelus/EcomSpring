package com.oebelus.shop.service.cartItem;

import com.oebelus.shop.exceptions.ResourceNotFoundException;
import com.oebelus.shop.model.Cart;
import com.oebelus.shop.model.CartItem;
import com.oebelus.shop.model.Product;
import com.oebelus.shop.repository.CartItemRepository;
import com.oebelus.shop.repository.CartRepository;
import com.oebelus.shop.service.cart.ICartService;
import com.oebelus.shop.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CartItemService implements ICartItemService {

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final IProductService productService;
    private final ICartService cartService;

    @Override
    public void addItem(Long cartId, Long productId, int quantity) {
        // Get the cart
        Cart cart = cartService.getCart(cartId);

        // Get the product
        Product product = productService.getProductById(productId);

        // Check if the product is already in the cart
        CartItem cartItem = cart.getCartItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst().orElse(new CartItem());

        // If not, add it
        if (cartItem.getId() == null) {
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setCart(cart);
            cartItem.setUnitPrice(product.getPrice());
        }
        // Else, update the total price
        else {
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
        }

        // Set the total price
        cartItem.setTotalPrice();

        // Save the cart
        cart.addItem(cartItem);
        cartItemRepository.save(cartItem);
        cartRepository.save(cart);
    }

    @Override
    public void updateQuantity(Long cartId, Long productId, int quantity) {
        // Get the cart
        Cart cart = cartService.getCart(cartId);

        // Get the cart item (product) to update
        cart.getCartItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst()
                .ifPresent(item -> {
                    item.setQuantity(quantity);
                    item.setUnitPrice(item.getProduct().getPrice());
                    item.setTotalPrice();
                });

        // Set the total price
        cart.setTotalPrice(cart.getTotalPrice());

        // Save the cart
        cartRepository.save(cart);
    }

    @Override
    public void deleteItem(Long cartId, Long productId) {
        // Get the card
        Cart cart = cartService.getCart(cartId);

        // Get the cart item (product) to remove
        CartItem cartItem = getCartItem(cartId, productId);

        // Remove the cart item
        cart.removeItem(cartItem);

        // Save the cart
        cartRepository.save(cart);
    }

    @Override
    public CartItem getCartItem(Long cartId, Long productId) {
        Cart cart = cartService.getCart(cartId);

        return cart.getCartItems()
                .stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst().orElseThrow(() -> new ResourceNotFoundException("Product Not Found!"));
    }
}
