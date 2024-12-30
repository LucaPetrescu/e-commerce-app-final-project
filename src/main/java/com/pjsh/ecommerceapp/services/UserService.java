package com.pjsh.ecommerceapp.services;

import com.pjsh.ecommerceapp.datamodels.User;
import com.pjsh.ecommerceapp.exceptions.UserAlreadyExistsException;
import com.pjsh.ecommerceapp.exceptions.UserDoesNotExistException;
import com.pjsh.ecommerceapp.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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
        User foundUser = userRepository.findByEmail(email);
        if(foundUser == null){
            throw new UserDoesNotExistException("No user with such email exists");
        }
        return foundUser;
    }

    public User getUserById(Integer id){
        Optional<User> foundUser = userRepository.findById(id);
        return foundUser.orElseThrow(() -> new UserDoesNotExistException("No user with such Id exists"));
    }

    public String deleteById(Integer id){
        this.userRepository.deleteById(id);
        return "User deleted successfully!";
    }

}
