package com.pjsh.ecommerceapp.services;

import com.pjsh.ecommerceapp.datamodels.Category;
import com.pjsh.ecommerceapp.datamodels.Product;
import com.pjsh.ecommerceapp.exceptions.ProductDoesNotExistException;
import com.pjsh.ecommerceapp.repositories.CategoryRepository;
import com.pjsh.ecommerceapp.repositories.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testUpdateProduct_success() {

        Integer productId = 1;
        Product existingProduct = new Product();
        existingProduct.setId(productId);
        existingProduct.setName("Old Name");

        when(productRepository.findById(productId)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Product updatedProduct = productService.updateProduct(productId, "New Name", null, 50, BigDecimal.valueOf(1000));

        assertEquals("New Name", updatedProduct.getName());
        assertEquals(50, updatedProduct.getAvailableQuantity());
        verify(productRepository).save(updatedProduct);
    }

    @Test
    void testUpdateProduct_productNotFound() {

        Integer productId = 1;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        ProductDoesNotExistException exception = assertThrows(ProductDoesNotExistException.class, () -> productService.updateProduct(productId, "Name", null, 50, BigDecimal.valueOf(1000)));
        assertEquals("No product with such id exists", exception.getMessage());
    }

    @Test
    void testGetProductById_success() {
        Integer productId = 11;
        Product expectedProduct = new Product();
        expectedProduct.setId(productId);
        expectedProduct.setName("Test Product");

        when(productRepository.findById(productId)).thenReturn(Optional.of(expectedProduct));

        Product actualProduct = productService.getProductById(productId);

        assertEquals(expectedProduct, actualProduct);
        verify(productRepository).findById(productId);
    }

    @Test
    void testIsProductEligibleForDiscount_notEligible() {
        Product product = new Product();
        product.setId(1);
        product.setPrice(BigDecimal.valueOf(150));

        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        Product actualProduct = productService.getProductById(product.getId());

        ReflectionTestUtils.setField(productService, "eligibilityExpression", "#price >= 200");

        boolean eligible = productService.isProductEligibleForDiscount(actualProduct.getId());

        assertFalse(eligible);
    }

    @Test
    void testAddProduct_success() {

        Product product = new Product();
        product.setName("Test Product");

        Integer categoryId = 1;
        Category category = new Category();
        category.setId(categoryId);
        category.setName("Electronics");
        category.setProducts(new ArrayList<>());

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(productRepository.findByName("Test Product")).thenReturn(null);
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        product.setCategory(category);

        Product result = productService.addProduct(product);

        assertNotNull(result);
        assertEquals("Test Product", result.getName());
        assertEquals(categoryId, result.getCategory().getId());
        verify(categoryRepository).findById(categoryId);
        verify(productRepository).save(product);
    }

    @Test
    void testGetProductByName_success() {
        String productName = "Test Product";
        Product product = new Product();
        product.setName(productName);

        when(productRepository.findByName(productName)).thenReturn(product);

        Product result = productService.getProductByName(productName);

        assertNotNull(result);
        assertEquals(productName, result.getName());
    }

    @Test
    void testGetProductByName_notFound() {
        String productName = "Nonexistent Product";

        when(productRepository.findByName(productName)).thenReturn(null);

        assertThrows(ProductDoesNotExistException.class, () -> productService.getProductByName(productName));
    }


}
