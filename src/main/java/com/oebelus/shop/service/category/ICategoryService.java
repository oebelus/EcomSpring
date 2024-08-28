package com.oebelus.shop.service.category;

import com.oebelus.shop.model.Category;

import java.util.List;

public interface ICategoryService {

    Category getCategoryById(Long id);

    Category getCategoryByName(String name);

    List<Category> getAllCategories();

    Category addCategory(Category category);

    Category updateCategory(Category category, Long id);

    void deleteCategory(Long id);
}
