package com.oebelus.shop.request;

import com.oebelus.shop.model.Category;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductUpdateRequest {
    private Long id;
    private String name;
    private String brand;
    private String description;
    private int inventory;
    private BigDecimal price;
    private Category category;
}
