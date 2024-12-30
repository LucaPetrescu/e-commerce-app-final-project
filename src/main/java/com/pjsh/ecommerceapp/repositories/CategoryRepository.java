package com.pjsh.ecommerceapp.repositories;

import com.pjsh.ecommerceapp.datamodels.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    Category findByName(String name);

}
