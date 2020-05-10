package com.ader.backend.service.category;

import com.ader.backend.entity.Category;
import com.ader.backend.helpers.BeanHelper;
import com.ader.backend.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private static final String CATEGORY_WITH_ID = "Category with id: [";
    private static final String CATEGORY_WITH_NAME = "Category with name: [";
    private static final String NOT_FOUND_ERROR_MESSAGE = "] not found";

    private final CategoryRepository categoryRepository;

    @Override
    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Category getCategory(Long id) {
        Category category = categoryRepository.findById(id).orElse(null);
        String errorMessage;

        if (category == null) {
            errorMessage = CATEGORY_WITH_ID + id + NOT_FOUND_ERROR_MESSAGE;
            log.error(errorMessage);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
        }

        return category;
    }

    @Override
    public Category getCategory(String name) {
        Category category = categoryRepository.findByName(name).orElse(null);
        String errorMessage;

        if (category == null) {
            errorMessage = CATEGORY_WITH_NAME + name + NOT_FOUND_ERROR_MESSAGE;
            log.error(errorMessage);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
        }

        return category;
    }

    @Override
    public Category createCategory(Category category) {
        String errorMessage;

        try {
            categoryRepository.save(category);
        } catch (Exception e) {
            errorMessage = e.getMessage();
            log.error(errorMessage);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
        }

        log.info("Created category: [{}]", category);
        return category;
    }

    @Override
    public Category updateCategory(Long id, Category category) {
        String errorMessage;
        Category categoryToUpdate = categoryRepository.findById(id).orElse(null);

        if (categoryToUpdate == null) {
            errorMessage = CATEGORY_WITH_ID + id + NOT_FOUND_ERROR_MESSAGE;
            log.error(errorMessage);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
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
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    errorMessage
            );
        }

        log.info("Category [{}] updated to [{}]", category, categoryToUpdate);
        return categoryToUpdate;
    }

    @Override
    public Category updateCategory(String name, Category category) {
        String errorMessage;
        Category categoryToUpdate = categoryRepository.findByName(name).orElse(null);

        if (categoryToUpdate == null) {
            errorMessage = CATEGORY_WITH_NAME + name + NOT_FOUND_ERROR_MESSAGE;
            log.error(errorMessage);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
        } else {
            return updateCategory(categoryToUpdate.getId(), category);
        }
    }

    @Override
    public String deleteCategory(Long id) {
        String errorMessage;
        Category categoryToDelete = categoryRepository.findById(id).orElse(null);

        if (categoryToDelete == null) {
            errorMessage = CATEGORY_WITH_ID + id + NOT_FOUND_ERROR_MESSAGE;
            log.error(errorMessage);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
        }

        try {
            categoryRepository.delete(categoryToDelete);
        } catch (Exception e) {
            errorMessage = e.getMessage();
            log.error(errorMessage);
            throw new ResponseStatusException(
                    HttpStatus.UNPROCESSABLE_ENTITY,
                    errorMessage
            );
        }

        String successMessage = "Category with id: [{" + id + "}] successfully deleted";
        log.info(successMessage);
        return successMessage;
    }

    @Override
    public String deleteCategory(String name) {
        String errorMessage;
        Category categoryToDelete = categoryRepository.findByName(name).orElse(null);

        if (categoryToDelete == null) {
            errorMessage = CATEGORY_WITH_ID + name + NOT_FOUND_ERROR_MESSAGE;
            log.error(errorMessage);
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMessage);
        } else {
            return deleteCategory(categoryToDelete.getId());
        }
    }
}
