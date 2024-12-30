package com.pjsh.ecommerceapp.repositories;

import com.pjsh.ecommerceapp.datamodels.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Integer> {
    Product findByName(String name);
}
