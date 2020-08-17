package com.ader.backend.rest;

import com.ader.backend.entity.Category;
import com.ader.backend.service.category.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class CategoryControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private CategoryService categoryService;

  private Category category;

  @BeforeEach
  void setUp() {
    category = new Category();
    category.setName("Test");
  }

  @Test
  @WithMockUser(username = "Test admin", roles = "ADMIN")
  void getCategories_whenInvoked_return200() throws Exception {
    mockMvc.perform(get("/rest/category")).andExpect(status().isOk());
  }

  @Test
  @WithAnonymousUser
  void getCategory_whenInvoked_return200() throws Exception {
    when(categoryService.getCategory(any(String.class))).thenReturn(category);

    mockMvc.perform(get("/rest/category/name/{name}", category.getName()))
            .andExpect(status().isOk());
  }

  @Test
  @WithMockUser(username = "test", roles = "ADMIN")
  void deleteCategory_whenInvoked_return200() throws Exception {
    when(categoryService.deleteCategory(any(String.class))).thenReturn("Success");

    mockMvc.perform(MockMvcRequestBuilders.delete("/rest/category/name/{name}", category.getName()))
            .andExpect(status().isOk());
  }
}
