package com.oebelus.shop.DTO;

import com.oebelus.shop.model.Product;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@AllArgsConstructor
@Data
public class OrderItemDTO {
    private Long id;
    private int quantity;
    private BigDecimal price;
    private Product product;
}
