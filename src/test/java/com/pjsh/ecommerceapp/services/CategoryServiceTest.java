package com.pjsh.ecommerceapp.services;

import com.pjsh.ecommerceapp.datamodels.Category;
import com.pjsh.ecommerceapp.exceptions.CategoryAlreadyExistsException;
import com.pjsh.ecommerceapp.exceptions.CategoryDoesNotExistException;
import com.pjsh.ecommerceapp.repositories.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryService categoryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddCategory_success() {

        Category category = new Category();
        category.setName("Electronics");

        when(categoryRepository.findByName("Electronics")).thenReturn(null);
        when(categoryRepository.save(category)).thenReturn(category);

        Category result = categoryService.addCategory(category);

        assertNotNull(result);
        assertEquals("Electronics", result.getName());
        verify(categoryRepository).save(category);
    }

    @Test
    void testAddCategory_categoryAlreadyExists() {

        Category category = new Category();
        category.setName("Electronics");

        when(categoryRepository.findByName("Electronics")).thenReturn(category);

        assertThrows(CategoryAlreadyExistsException.class, () -> categoryService.addCategory(category));
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    void testGetCategoryByName_success() {

        Category category = new Category();
        category.setName("Electronics");

        when(categoryRepository.findByName("Electronics")).thenReturn(category);

        Category result = categoryService.getCategoryByName("Electronics");

        assertNotNull(result);
        assertEquals("Electronics", result.getName());
        verify(categoryRepository).findByName("Electronics");
    }

    @Test
    void testGetCategoryByName_categoryDoesNotExist() {
        when(categoryRepository.findByName("Nonexistent")).thenReturn(null);

        assertThrows(CategoryDoesNotExistException.class, () -> categoryService.getCategoryByName("Nonexistent"));
        verify(categoryRepository).findByName("Nonexistent");
    }

    @Test
    void testGetCategoryById_success() {

        Category category = new Category();
        category.setId(1);
        category.setName("Electronics");

        when(categoryRepository.findById(1)).thenReturn(Optional.of(category));

        Category result = categoryService.getCategoryById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Electronics", result.getName());
        verify(categoryRepository).findById(1);
    }

    @Test
    void testGetCategoryById_categoryDoesNotExist() {

        when(categoryRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(CategoryDoesNotExistException.class, () -> categoryService.getCategoryById(999));
        verify(categoryRepository).findById(999);
    }

    @Test
    void testDeleteById_success() {
        Integer categoryId = 1;

        String result = categoryService.deleteById(categoryId);

        assertEquals("Category deleted successfully!", result);
        verify(categoryRepository).deleteById(categoryId);
    }

}
