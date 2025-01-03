package com.pjsh.ecommerceapp.repositories;

import com.pjsh.ecommerceapp.datamodels.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

}
