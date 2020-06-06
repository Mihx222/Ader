package com.ader.backend.service.category;

import com.ader.backend.entity.Category;
import com.ader.backend.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplTest {

  @Mock
  private CategoryRepository categoryRepository;

  @InjectMocks
  private CategoryServiceImpl categoryService;

  private List<Category> categories;
  private Category category1;
  private Category category2;

  @BeforeEach
  void setUp() {
    categoryService = new CategoryServiceImpl(categoryRepository);

    categories = new ArrayList<>();

    category1 = new Category();
    category1.setId(1L);
    category1.setName("Test 1");
    category2 = new Category();
    category2.setId(1L);
    category2.setName("Test 2");
  }

  @Test
  void getAllCategories_whenInvoked_returnAllCategories() {
    categories.addAll(Arrays.asList(category1, category2));
    when(categoryRepository.findAll()).thenReturn(categories);

    List<Category> categories = categoryService.getAllCategories();

    assertThat(categories).hasSize(2);
  }

  @Test
  void getCategory_whenInvoked_returnCategory() {
    categories.addAll(Arrays.asList(category1, category2));
    when(categoryRepository.findById(any(Long.class))).thenReturn(java.util.Optional.ofNullable(category1));
    when(categoryRepository.findByName(any(String.class))).thenReturn(java.util.Optional.ofNullable(category2));

    Category byId = categoryService.getCategory(category1.getId());
    Category byName = categoryService.getCategory(category2.getName());

    assertThat(byId).isNotNull();
    assertThat(byId).isEqualTo(category1);
    assertThat(byName).isNotNull();
    assertThat(byName).isEqualTo(category2);
  }

  @Test
  void createCategory_whenInvoked_callSaveOneTime() {
    categoryService.createCategory(category1);

    verify(categoryRepository, times(1)).save(category1);
  }

  @Test
  void updateCategory_whenInvoked_doesNotThrowAnyException() {
    when(categoryRepository.findById(any(Long.class))).thenReturn(java.util.Optional.ofNullable(category1));
    when(categoryRepository.findByName(any(String.class))).thenReturn(java.util.Optional.ofNullable(category2));

    assertThatCode(() -> categoryService.updateCategory(category1.getId(), category2)).doesNotThrowAnyException();
    assertThatCode(() -> categoryService.updateCategory(category2.getName(), category1)).doesNotThrowAnyException();
  }

  @Test
  void deleteCategory_whenInvoked_callDeleteOnlyOnce() {
    when(categoryRepository.findById(any(Long.class))).thenReturn(java.util.Optional.ofNullable(category1));
    when(categoryRepository.findByName(any(String.class))).thenReturn(java.util.Optional.ofNullable(category2));

    assertThatCode(() -> categoryService.deleteCategory(category1.getId())).doesNotThrowAnyException();
    assertThatCode(() -> categoryService.deleteCategory(category2.getName())).doesNotThrowAnyException();
  }
}
