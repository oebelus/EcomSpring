package com.oebelus.shop.controller;

import com.oebelus.shop.exceptions.ResourceNotFoundException;
import com.oebelus.shop.response.ApiResponse;
import com.oebelus.shop.service.cart.CartService;
import com.oebelus.shop.service.cart.ICartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Service
@RequiredArgsConstructor // Constructor Injection
public class CartController {
    private final ICartService cartService;

    @GetMapping("/{id}/my-cart")
    public ResponseEntity<ApiResponse> getCart(Long id) {
        try {
            return ResponseEntity.ok(new ApiResponse("Get Cart Success!", cartService.getCart(id)));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @PostMapping("/{id}/clear")
    public ResponseEntity<ApiResponse> clearCart(Long id) {
        try {
            cartService.clearCart(id);
            return ResponseEntity.ok(new ApiResponse("Clear Cart Success!", null));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/{id}/cart/total")
    public ResponseEntity<ApiResponse> getTotalPrice(Long id) {
        try {
            return ResponseEntity.ok(new ApiResponse("Success!", cartService.getTotalPrice(id)));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
