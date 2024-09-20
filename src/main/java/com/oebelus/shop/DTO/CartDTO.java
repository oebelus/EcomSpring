package com.oebelus.shop.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@AllArgsConstructor
@Data
public class CartDTO {
    private Long cardId;
    private Set<CartItemDTO> cartItems;
    private BigDecimal totalPrice;
}
