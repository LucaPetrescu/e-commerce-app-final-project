package com.pjsh.ecommerceapp.services;

import com.pjsh.ecommerceapp.datamodels.User;
import com.pjsh.ecommerceapp.exceptions.UserAlreadyExistsException;
import com.pjsh.ecommerceapp.exceptions.UserDoesNotExistException;
import com.pjsh.ecommerceapp.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegisterUser_success() {

        User user = new User();
        user.setEmail("test@example.com");

        when(userRepository.findByEmail("test@example.com")).thenReturn(null);
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.registerUser(user);

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        verify(userRepository).save(user);
    }

    @Test
    void testRegisterUser_userAlreadyExists() {

        User user = new User();
        user.setEmail("test@example.com");

        when(userRepository.findByEmail("test@example.com")).thenReturn(user);

        assertThrows(UserAlreadyExistsException.class, () -> userService.registerUser(user));
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testGetUserByEmail_success() {

        User user = new User();
        user.setEmail("test@example.com");

        when(userRepository.findByEmail("test@example.com")).thenReturn(user);

        User result = userService.getUserByEmail("test@example.com");

        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        verify(userRepository).findByEmail("test@example.com");
    }

    @Test
    void testGetUserByEmail_userDoesNotExist() {

        when(userRepository.findByEmail("nonexistent@example.com")).thenReturn(null);

        assertThrows(UserDoesNotExistException.class, () -> userService.getUserByEmail("nonexistent@example.com"));
        verify(userRepository).findByEmail("nonexistent@example.com");
    }

    @Test
    void testGetUserById_success() {

        User user = new User();
        user.setId(1);
        user.setEmail("test@example.com");

        when(userRepository.findById(1)).thenReturn(Optional.of(user));

        User result = userService.getUserById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("test@example.com", result.getEmail());
        verify(userRepository).findById(1);
    }

    @Test
    void testGetUserById_userDoesNotExist() {

        when(userRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(UserDoesNotExistException.class, () -> userService.getUserById(999));
        verify(userRepository).findById(999);
    }

    @Test
    void testDeleteById_success() {

        Integer userId = 1;

        String result = userService.deleteById(userId);

        assertEquals("User deleted successfully!", result);
        verify(userRepository).deleteById(userId);
    }

}
