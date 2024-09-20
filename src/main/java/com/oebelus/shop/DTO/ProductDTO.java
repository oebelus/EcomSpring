package com.oebelus.shop.DTO;

import com.oebelus.shop.model.Category;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@Data
public class ProductDTO {
    private Long id;
    private String name;
    private String brand;
    private String description;
    private int inventory;
    private BigDecimal price;
    private Category category;
    private List<ImageDTO> images;
}
