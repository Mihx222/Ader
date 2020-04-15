package com.ader.backend.service.category;

import com.ader.backend.entity.category.Category;
import com.ader.backend.entity.category.CategoryDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CategoryService {

    ResponseEntity<List<CategoryDto>> getAllCategories();

    ResponseEntity<Object> getCategory(Long id);

    ResponseEntity<Object> getCategory(String name);

    ResponseEntity<Object> createCategory(Category category);

    ResponseEntity<Object> updateCategory(Long id, Category category);

    ResponseEntity<Object> updateCategory(String name, Category category);

    ResponseEntity<Object> deleteCategory(Long id);

    ResponseEntity<Object> deleteCategory(String name);
}
