package com.ader.backend.service.category;

import com.ader.backend.entity.category.Category;
import com.ader.backend.entity.category.CategoryDto;
import com.ader.backend.helpers.BeanHelper;
import com.ader.backend.repository.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@Slf4j
public class CategoryServiceImpl implements CategoryService {

    private static final String CATEGORY_WITH_ID = "Category with id: [";
    private static final String CATEGORY_WITH_NAME = "Category with name: [";
    private static final String NOT_FOUND_ERROR_MESSAGE = "] not found";

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public ResponseEntity<List<CategoryDto>> getAllCategories() {
        return ResponseEntity.ok(CategoryDto.toDto(categoryRepository.findAll()));
    }

    @Override
    public ResponseEntity<Object> getCategory(Long id) {
        Category category = categoryRepository.findById(id).orElse(null);
        String errorMessage;

        if (category == null) {
            errorMessage = CATEGORY_WITH_ID + id + NOT_FOUND_ERROR_MESSAGE;
            log.error(errorMessage);
            return ResponseEntity.badRequest().body(errorMessage);
        }

        return ResponseEntity.ok(CategoryDto.toDto(category));
    }

    @Override
    public ResponseEntity<Object> getCategory(String name) {
        Category category = categoryRepository.findByName(name).orElse(null);
        String errorMessage;

        if (category == null) {
            errorMessage = CATEGORY_WITH_NAME + name + NOT_FOUND_ERROR_MESSAGE;
            log.error(errorMessage);
            return ResponseEntity.badRequest().body(errorMessage);
        }

        return ResponseEntity.ok(CategoryDto.toDto(category));
    }

    @Override
    public ResponseEntity<Object> createCategory(Category category) {
        String errorMessage;

        try {
            categoryRepository.save(category);
        } catch (Exception e) {
            errorMessage = e.getMessage();
            log.error(errorMessage);
            return ResponseEntity.unprocessableEntity().body(category);
        }

        log.info("Created category: [{}]", category);
        return ResponseEntity.ok(CategoryDto.toDto(category));
    }

    @Override
    public ResponseEntity<Object> updateCategory(Long id, Category category) {
        String errorMessage;
        Category categoryToUpdate = categoryRepository.findById(id).orElse(null);

        if (categoryToUpdate == null) {
            errorMessage = CATEGORY_WITH_ID + id + NOT_FOUND_ERROR_MESSAGE;
            log.error(errorMessage);
            return ResponseEntity.badRequest().body(errorMessage);
        }

        try {
            BeanUtils.copyProperties(
                    category,
                    categoryToUpdate,
                    BeanHelper.getNullPropertyNames(category, true)
            );

            categoryRepository.save(category);
        } catch (Exception e) {
            errorMessage = e.getMessage();
            log.error(errorMessage);
            return ResponseEntity.unprocessableEntity().body(errorMessage);
        }

        log.info("Category [{}] updated to [{}]", category, categoryToUpdate);
        return ResponseEntity.ok(CategoryDto.toDto(categoryToUpdate));
    }

    @Override
    public ResponseEntity<Object> updateCategory(String name, Category category) {
        String errorMessage;
        Category categoryToUpdate = categoryRepository.findByName(name).orElse(null);

        if (categoryToUpdate == null) {
            errorMessage = CATEGORY_WITH_NAME + name + NOT_FOUND_ERROR_MESSAGE;
            log.error(errorMessage);
            return ResponseEntity.badRequest().body(errorMessage);
        } else {
            return updateCategory(categoryToUpdate.getId(), category);
        }
    }

    @Override
    public ResponseEntity<Object> deleteCategory(Long id) {
        String errorMessage;
        Category categoryToDelete = categoryRepository.findById(id).orElse(null);

        if (categoryToDelete == null) {
            errorMessage = CATEGORY_WITH_ID + id + NOT_FOUND_ERROR_MESSAGE;
            log.error(errorMessage);
            return ResponseEntity.badRequest().body(errorMessage);
        }

        try {
            categoryRepository.delete(categoryToDelete);
        } catch (Exception e) {
            errorMessage = e.getMessage();
            log.error(errorMessage);
            return ResponseEntity.unprocessableEntity().body(errorMessage);
        }

        String successMessage = "Category with id: [{" + id + "}] successfully deleted";
        log.info(successMessage);
        return ResponseEntity.ok(successMessage);
    }

    @Override
    public ResponseEntity<Object> deleteCategory(String name) {
        String errorMessage;
        Category categoryToDelete = categoryRepository.findByName(name).orElse(null);

        if (categoryToDelete == null) {
            errorMessage = CATEGORY_WITH_ID + name + NOT_FOUND_ERROR_MESSAGE;
            log.error(errorMessage);
            return ResponseEntity.badRequest().body(errorMessage);
        } else {
            return deleteCategory(categoryToDelete.getId());
        }
    }
}
