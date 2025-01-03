package com.pjsh.ecommerceapp.services;

import com.pjsh.ecommerceapp.datamodels.User;

import com.pjsh.ecommerceapp.exceptions.UserAlreadyExistsException;
import com.pjsh.ecommerceapp.exceptions.UserDoesNotExistException;
import com.pjsh.ecommerceapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;


    public User registerUser(User user){
        if(userRepository.findByEmail(user.getEmail()) != null){
            throw new UserAlreadyExistsException("User with this email already exists!");
        }
        return userRepository.save(user);
    }

    public User getUserByEmail(String email) {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new UserDoesNotExistException("No user with such email exists");
        }

        return user;
    }

    public User getUserById(Integer id){
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserDoesNotExistException("No user with such Id exists"));

        return user;
    }

    public String deleteById(Integer id){
        this.userRepository.deleteById(id);
        return "User deleted successfully!";
    }

}
