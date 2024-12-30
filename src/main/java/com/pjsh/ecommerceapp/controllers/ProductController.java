package com.pjsh.ecommerceapp.controllers;

import com.pjsh.ecommerceapp.daos.ProductUpdateDAO;
import com.pjsh.ecommerceapp.datamodels.Product;
import com.pjsh.ecommerceapp.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/addProduct")
    public ResponseEntity<Product> addProduct(@RequestBody Product product){
        return ResponseEntity.ok(this.productService.addProduct(product));
    }

    @GetMapping("/getProductById/{product_id}")
    public ResponseEntity<Product> gerProductById(@PathVariable Integer productId){
        return ResponseEntity.ok(this.productService.getProductById(productId));
    }

    @GetMapping("/getProductByName")
    public ResponseEntity<Product> getProductByName(@RequestBody String name){
        return ResponseEntity.ok(this.productService.getProductByName(name));
    }

    @DeleteMapping("/deleteProduct/{product_id}")
    public ResponseEntity<String> deleteById(@PathVariable("product_id") Integer productId){
        return ResponseEntity.ok(this.productService.deleteById(productId));
    }

    @GetMapping("/eligibleForDiscount/{productId}")
    public boolean isEligibleForDiscount(@PathVariable Integer productId){
        return productService.isProductEligibleForDiscount(productId);
    }

    @PatchMapping("/updateProduct/{productId}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Integer productId,
            @RequestBody ProductUpdateDAO updateRequest) {

        Product updatedProduct = productService.updateProduct(
                productId,
                updateRequest.getName(),
                updateRequest.getDescription(),
                updateRequest.getStock(),
                updateRequest.getPrice()
        );

        return ResponseEntity.ok(updatedProduct);
    }

}
