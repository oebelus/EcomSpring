package com.oebelus.shop.service.product;

import com.oebelus.shop.model.Product;
import com.oebelus.shop.request.AddProductRequest;
import com.oebelus.shop.request.ProductUpdateRequest;

import java.util.List;

public interface IProductService {
    Product addProduct(AddProductRequest request);

    Product getProductById(Long id);

    void updateProduct(ProductUpdateRequest request, Long productId);

    void deleteProduct(Long id);

    List<Product> getAllProducts();

    List<Product> getProductsByCategory(String category);

    List<Product> getProductsByBrand(String brand);

    List<Product> getProductByCategoryAndBrand(String category, String brand);

    List<Product> getProductsByName(String name);

    List<Product> getProductByBrandAndName(String brand, String name);

    Long countProductsByBrandAndName(String brand, String name);
}
