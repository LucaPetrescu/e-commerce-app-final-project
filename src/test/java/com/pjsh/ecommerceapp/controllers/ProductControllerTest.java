package com.pjsh.ecommerceapp.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.pjsh.ecommerceapp.dtos.ProductUpdateDTO;
import com.pjsh.ecommerceapp.datamodels.Product;
import com.pjsh.ecommerceapp.services.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    void testAddProduct_success() throws Exception {

        Product product = new Product();
        product.setId(1);
        product.setName("New Product");
        product.setPrice(BigDecimal.valueOf(150));

        when(productService.addProduct(any(Product.class))).thenReturn(product);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        mockMvc.perform(post("/product/addProduct")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("New Product"))
                .andExpect(jsonPath("$.price").value(150));

        verify(productService).addProduct(any(Product.class));
    }

    @Test
    void testGetProductById_success() throws Exception {

        Product product = new Product();
        product.setId(1);
        product.setName("Test Product");
        product.setPrice(BigDecimal.valueOf(100));

        when(productService.getProductById(1)).thenReturn(product);

        mockMvc.perform(get("/product/getProductById/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Product"))
                .andExpect(jsonPath("$.price").value(100));

        verify(productService).getProductById(1);
    }

    @Test
    void testGetProductByName_success() throws Exception {

        String productName = "Test Product";
        Product product = new Product();
        product.setId(1);
        product.setName(productName);
        product.setPrice(BigDecimal.valueOf(100));

        when(productService.getProductByName(productName)).thenReturn(product);

        mockMvc.perform(get("/product/getProductByName/{name}", productName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Product"))
                .andExpect(jsonPath("$.price").value(100));

        verify(productService).getProductByName(productName);
    }

    @Test
    void testUpdateProduct_success() throws Exception {
        Product product = new Product();
        product.setId(1);
        product.setName("Updated Product");
        product.setPrice(BigDecimal.valueOf(200));

        ProductUpdateDTO updateRequest = new ProductUpdateDTO();
        updateRequest.setName("Updated Product");
        updateRequest.setPrice(BigDecimal.valueOf(200));
        updateRequest.setStock(50);

        when(productService.updateProduct(anyInt(), any(), any(), any(), any())).thenReturn(product);

        mockMvc.perform(patch("/product/updateProduct/{productId}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Updated Product"))
                .andExpect(jsonPath("$.price").value(200));

        verify(productService).updateProduct(eq(1), eq("Updated Product"), any(), eq(50), eq(BigDecimal.valueOf(200)));
    }

    @Test
    void testDeleteProduct_success() throws Exception {
        when(productService.deleteById(1)).thenReturn("Product deleted successfully!");

        mockMvc.perform(delete("/product/deleteProduct/{product_id}", 1)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("Product deleted successfully!"));

        verify(productService).deleteById(1);
    }
}
