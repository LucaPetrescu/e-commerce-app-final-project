package com.pjsh.ecommerceapp.repositories;

import com.pjsh.ecommerceapp.datamodels.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);
}
