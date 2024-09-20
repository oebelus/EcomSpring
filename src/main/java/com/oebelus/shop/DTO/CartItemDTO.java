package com.oebelus.shop.DTO;

import com.oebelus.shop.model.Product;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@AllArgsConstructor
@Data
public class CartItemDTO {
    private Long id;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    private int quantity;
    private ProductDTO product;
}
