package com.oebelus.shop.controller;

import com.oebelus.shop.exceptions.ResourceNotFoundException;
import com.oebelus.shop.response.ApiResponse;
import com.oebelus.shop.service.cart.ICartService;
import com.oebelus.shop.service.cartItem.ICartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/cartItems/")
public class CartItemController {

    private final ICartItemService cartItemService;
    private final ICartService cartService;

    @PostMapping("item/add")
    public ResponseEntity<ApiResponse> addItem(@RequestParam(required = false) Long cartId, @RequestParam Long productId, @RequestParam int quantity) {
        try {
            // If no cart is generated, we generate a cart ID
            if (cartId == null) cartId = cartService.initializeNewCart();

            cartItemService.addItem(cartId, productId, quantity);
            return ResponseEntity.ok(new ApiResponse("Add item success!", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PutMapping("cart/{cartId}/item/{itemId}/update")
    public ResponseEntity<ApiResponse> updateItem(@PathVariable Long cartId, @PathVariable Long itemId, @RequestParam int quantity) {
        try {
            cartItemService.updateQuantity(cartId, itemId, quantity);
            return ResponseEntity.ok(new ApiResponse("Update item success!", null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("cart/{cartId}/item/{itemId}/delete")
    public ResponseEntity<ApiResponse> deleteItem(@PathVariable Long cartId, @PathVariable Long itemId) {
        try {
            cartItemService.deleteItem(cartId, itemId);
            return ResponseEntity.ok(new ApiResponse("Delete item success!", null));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
