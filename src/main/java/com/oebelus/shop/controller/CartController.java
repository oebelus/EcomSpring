package com.oebelus.shop.controller;

import com.oebelus.shop.exceptions.ResourceNotFoundException;
import com.oebelus.shop.response.ApiResponse;
import com.oebelus.shop.service.cart.ICartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor // Constructor Injection
@RestController
@RequestMapping("api/v1/carts")
public class CartController {
    private final ICartService cartService;

    @GetMapping("/{id}/my-cart")
    public ResponseEntity<ApiResponse> getCart(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(new ApiResponse("Get Cart Success!", cartService.getCart(id)));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @DeleteMapping("/{id}/clear")
    public ResponseEntity<ApiResponse> clearCart(@PathVariable Long id) {
        try {
            cartService.clearCart(id);
            return ResponseEntity.ok(new ApiResponse("Clear Cart Success!", null));
        } catch (Exception e) {
            return ResponseEntity.status(NOT_FOUND).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/{id}/cart/total")
    public ResponseEntity<ApiResponse> getTotalPrice(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(new ApiResponse("Success!", cartService.getTotalPrice(id)));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
