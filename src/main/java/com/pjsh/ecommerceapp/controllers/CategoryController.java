package com.pjsh.ecommerceapp.controllers;

import com.pjsh.ecommerceapp.datamodels.Category;
import com.pjsh.ecommerceapp.datamodels.Product;
import com.pjsh.ecommerceapp.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping("/addCategory")
    public ResponseEntity<Category> addCategory(@RequestBody Category category){
        return ResponseEntity.ok(this.categoryService.addCategory(category));
    }

    @GetMapping("/getCategoryById/{category_id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Integer categoryId){
        return ResponseEntity.ok(this.categoryService.getCategoryById(categoryId));
    }

    @GetMapping("/getCategoryByName")
    public ResponseEntity<Category> getCategoryByName(@RequestBody String name){
        return ResponseEntity.ok(this.categoryService.getCategoryByName(name));
    }

    @DeleteMapping("/deleteCategory/{category_id}")
    public ResponseEntity<String> deleteById(@PathVariable("category_id") Integer categoryId){
        return ResponseEntity.ok(this.categoryService.deleteById(categoryId));
    }

}
