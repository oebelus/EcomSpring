package com.oebelus.shop.controller;

import com.oebelus.shop.DTO.OrderDTO;
import com.oebelus.shop.exceptions.ResourceNotFoundException;
import com.oebelus.shop.model.Order;
import com.oebelus.shop.response.ApiResponse;
import com.oebelus.shop.service.order.IOrderService;
import com.oebelus.shop.service.user.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/orders")
public class OrderController {

    private final IOrderService orderService;

    @PostMapping("/create-order")
    public ResponseEntity<ApiResponse> CreateOrder(@RequestParam Long userId) {
        try {
            OrderDTO orderDto = orderService.createOrder(userId);
            return ResponseEntity.ok(new ApiResponse("Order created successfully", orderDto));
        } catch (Exception e) {
            return ResponseEntity.status(INTERNAL_SERVER_ERROR).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/{orderId}/get-order")
    public ResponseEntity<ApiResponse> getOrder(@PathVariable Long orderId) {
        try {
            OrderDTO order = orderService.getOrder(orderId);
            return ResponseEntity.ok(new ApiResponse("Order retrieved successfully", order));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(500).body(new ApiResponse(e.getMessage(), null));
        }
    }

    @GetMapping("/{userId}/get-orders-by-user")
    public ResponseEntity<ApiResponse> getOrdersByUserId(@PathVariable Long userId) {
        try {
            List<OrderDTO> orders = orderService.getOrdersByUserId(userId);
            return ResponseEntity.ok(new ApiResponse("Orders retrieved successfully", orders));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(500).body(new ApiResponse(e.getMessage(), null));
        }
    }
}
