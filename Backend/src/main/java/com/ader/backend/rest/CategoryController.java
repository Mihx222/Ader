package com.ader.backend.rest;

import com.ader.backend.entity.Category;
import com.ader.backend.rest.dto.CategoryDto;
import com.ader.backend.service.category.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("rest/category")
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    public ResponseEntity<List<CategoryDto>> getCategories() {
        log.info("Requested all categories");
        return ResponseEntity.ok(CategoryDto.toDto(categoryService.getAllCategories()));
    }

    @GetMapping("{id}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable Long id) {
        log.info("Requested category with id: [{}]", id);
        return ResponseEntity.ok(CategoryDto.toDto(categoryService.getCategory(id)));
    }

    @GetMapping("name/{name}")
    public ResponseEntity<CategoryDto> getCategory(@PathVariable String name) {
        log.info("Requested category with name: [{}]", name);
        return ResponseEntity.ok(CategoryDto.toDto(categoryService.getCategory(name)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("add")
    public ResponseEntity<CategoryDto> createCategory(@RequestBody Category category) {
        log.info("Requested creating new category with payload: [{}]", category);
        return ResponseEntity.ok(CategoryDto.toDto(categoryService.createCategory(category)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("{id}")
    public ResponseEntity<CategoryDto> editCategory(@PathVariable Long id, @RequestBody Category category) {
        log.info("Requested updating category with id: [{}], new payload: [{}]", id, category);
        return ResponseEntity.ok(CategoryDto.toDto(categoryService.updateCategory(id, category)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("name/{name}")
    public ResponseEntity<CategoryDto> editCategory(@PathVariable String name, @RequestBody Category category) {
        log.info("Requested updating category with name: [{}], new payload: [{}]", name, category);
        return ResponseEntity.ok(CategoryDto.toDto(categoryService.updateCategory(name, category)));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable Long id) {
        log.info("Requested deleting category with id: [{}]", id);
        return ResponseEntity.ok(categoryService.deleteCategory(id));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("name/{name}")
    public ResponseEntity<String> deleteCategory(@PathVariable String name) {
        log.info("Requested deleting category with name: [{}]", name);
        return ResponseEntity.ok(categoryService.deleteCategory(name));
    }
}
