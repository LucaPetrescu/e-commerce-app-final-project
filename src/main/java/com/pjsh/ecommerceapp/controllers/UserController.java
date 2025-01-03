package com.pjsh.ecommerceapp.controllers;

import com.pjsh.ecommerceapp.datamodels.User;
import com.pjsh.ecommerceapp.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestBody User user){
        return ResponseEntity.ok(this.userService.registerUser(user));
    }

    @GetMapping("/getUserByEmail/{email}")
    public ResponseEntity<User> getUserByEmail(@PathVariable("email") String email){
        return ResponseEntity.ok(this.userService.getUserByEmail(email));
    }

    @GetMapping("/getUserById/{user_id}")
    public ResponseEntity<User> getUserById(@PathVariable("user_id") Integer userId){
        return ResponseEntity.ok(this.userService.getUserById(userId));
    }

    @DeleteMapping("/deleteUser/{user_id}")
    public ResponseEntity<String> deleteById(@PathVariable("user_id") Integer userId){
        return ResponseEntity.ok(this.userService.deleteById(userId));
    }

}
