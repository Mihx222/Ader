package com.ader.backend.service.category;

import com.ader.backend.entity.Category;

import java.util.List;

public interface CategoryService {

    List<Category> getAllCategories();

    Category getCategory(Long id);

    Category getCategory(String name);

    Category createCategory(Category category);

    Category updateCategory(Long id, Category category);

    Category updateCategory(String name, Category category);

    String deleteCategory(Long id);

    String deleteCategory(String name);
}
