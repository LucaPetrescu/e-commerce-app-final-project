package com.pjsh.ecommerceapp.repositories;

import com.pjsh.ecommerceapp.datamodels.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Integer> {

}
