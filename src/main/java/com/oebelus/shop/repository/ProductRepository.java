package com.oebelus.shop.repository;

import com.oebelus.shop.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategoryName(String category);

    List<Product> findByBrand(String brand);

    List<Product> findByCategoryNameAndBrand(String category, String brand);

    List<Product> findByNane(String name);

    List<Product> findByBrandAndName(String category, String name);

    Long countByBrandAndName(String brand, String name);
}
