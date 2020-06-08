package com.ader.backend.rest;

import com.ader.backend.entity.Category;
import com.ader.backend.service.category.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class CategoryControllerTest {

  @Mock
  private CategoryService categoryService;

  @InjectMocks
  private CategoryController categoryController;

  private MockMvc mockMvc;
  private Category category;

  @BeforeEach
  void setUp() {
    mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();

    category = new Category();
    category.setName("Test");
  }

  @Test
  void getCategories_whenInvoked_return200() throws Exception {
    mockMvc.perform(get("/rest/category")).andExpect(status().isOk());
  }

  @Test
  void getCategory_whenInvoked_return200() throws Exception {
    when(categoryService.getCategory(any(String.class))).thenReturn(category);

    mockMvc.perform(get("/rest/category/name/{name}", category.getName()))
            .andExpect(status().isOk());
  }

  @Test
  void createCategory_whenInvoked_return200() throws Exception {
    when(categoryService.createCategory(any(Category.class))).thenReturn(category);

    mockMvc.perform(post("/rest/category/add").content(category.toString()))
            .andExpect(status().isOk());
  }

  @Test
  void editCategory_whenInvoked_return200() throws Exception {
    when(categoryService.updateCategory(any(String.class), any(Category.class))).thenReturn(category);

    mockMvc.perform(put("/rest/category/name/{name}", category.getName()).content(category.toString()))
            .andExpect(status().isOk());
  }

  @Test
  void deleteCategory_whenInvoked_return200() throws Exception {
    when(categoryService.deleteCategory(any(String.class))).thenReturn("Success");

    mockMvc.perform(delete("/rest/category/name/{name}", category.getName()))
            .andExpect(status().isOk());
  }
}
