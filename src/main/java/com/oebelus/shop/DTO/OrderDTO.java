package com.oebelus.shop.DTO;

import com.oebelus.shop.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

@Data
@AllArgsConstructor
public class OrderDTO {
    private Long id;
    private Long userId;
    private LocalDate orderDate;
    private BigDecimal totalPrice;
    private OrderStatus orderStatus;
    private Set<OrderItemDTO> orderItems;
}
