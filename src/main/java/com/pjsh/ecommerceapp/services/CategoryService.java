package com.pjsh.ecommerceapp.services;

import com.pjsh.ecommerceapp.datamodels.Category;
import com.pjsh.ecommerceapp.datamodels.User;
import com.pjsh.ecommerceapp.exceptions.CategoryAlreadyExistsException;
import com.pjsh.ecommerceapp.exceptions.CategoryDoesNotExistException;
import com.pjsh.ecommerceapp.exceptions.UserDoesNotExistException;
import com.pjsh.ecommerceapp.repositories.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public Category addCategory(Category category){
        if(categoryRepository.findByName(category.getName()) != null){
            throw new CategoryAlreadyExistsException("Category with this name already exists");
        }
        return categoryRepository.save(category);
    }

    public Category getCategoryByName(String name){
        Category foundCategory = categoryRepository.findByName(name);
        if(foundCategory == null){
            throw new CategoryDoesNotExistException("No category with such name exists");
        }
        return foundCategory;
    }

    public Category getCategoryById(Integer id){
        Optional<Category> foundCategory = categoryRepository.findById(id);
        return foundCategory.orElseThrow(() -> new CategoryDoesNotExistException("No category with such Id exists"));
    }

    public String deleteById(Integer id){
        this.categoryRepository.deleteById(id);
        return "Category deleted successfully!";
    }

}
