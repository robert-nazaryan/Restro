package org.restro.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.restro.entity.User;
import org.restro.entity.UserType;
import org.restro.exception.UserNotFoundException;
import org.restro.repository.UserRepository;
import org.restro.service.SendMailService;
import org.restro.service.WeeklyEmailService;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SendMailService sendMailService;

    @Mock
    private WeeklyEmailService weeklyEmailService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository, sendMailService, weeklyEmailService, passwordEncoder);
    }

    @Test
    void testSave() {
        User user = new User();
        user.setEmail("poxos@gmail.com");
        user.setPassword("password");
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        userService.save(user);

        verify(userRepository).save(user);
        assertEquals("encodedPassword", user.getPassword());
    }

    @Test
    void testRegister() {
        User user = new User();
        user.setEmail("poxos@gmail.com");
        userService.register(user);

        verify(sendMailService).sendWelcomeMail(user);
        verify(userRepository).save(user);
        verify(weeklyEmailService).save(any());
        assertFalse(user.isActive());
        assertNotNull(user.getToken());
    }

    @Test
    void testFindAll() {
        List<User> userList = new ArrayList<>();
        userList.add(new User());
        userList.add(new User());
        when(userRepository.findAll()).thenReturn(userList);
        List<User> result = userService.findAll();

        assertEquals(2, result.size());
    }

    @Test
    void testFindByEmail() {
        User user = new User();
        user.setEmail("poxos@gmail.com");
        when(userRepository.findByEmail("poxos@gmail.com")).thenReturn(Optional.of(user));
        User result = userService.findByEmail("poxos@gmail.com");

        assertEquals("poxos@gmail.com", result.getEmail());
    }

    @Test
    void testFindByEmailWhenThrowUserNotFoundException() {
        when(userRepository.findByEmail("poxos@gmail.com")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findByEmail("poxos@gmail.com"));
    }

    @Test
    void testFindByToken() {
        User user = new User();
        user.setToken("token");
        when(userRepository.findByToken("token")).thenReturn(Optional.of(user));
        User result = userService.findByToken("token");

        assertEquals("token", result.getToken());
    }

    @Test
    void testFindByTokenWhenThrowUserNotFoundException() {
        when(userRepository.findByToken("token")).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findByToken("token"));
    }

    @Test
    void testByEmail() {
        String email = "poxos@gmail.com";
        Optional<User> userOptional = Optional.of(new User());
        when(userRepository.findByEmail(email)).thenReturn(userOptional);
        Optional<User> result = userService.byEmail(email);

        assertTrue(result.isPresent());
    }

    @Test
    void testFindByUserType() {
        List<User> users = new ArrayList<>();
        users.add(new User());
        when(userRepository.findAllByUserType(UserType.USER)).thenReturn(users);
        List<User> result = userService.findByUserType(UserType.USER);

        assertEquals(1, result.size());
    }

    @Test
    void testDeleteUserById() {
        int id = 1;
        userService.deleteUserById(id);

        verify(userRepository).deleteById(id);
    }

    @Test
    void testFindById() {
        int id = 1;
        Optional<User> userOptional = Optional.of(new User());
        when(userRepository.findById(id)).thenReturn(userOptional);
        Optional<User> result = userService.findById(id);

        assertTrue(result.isPresent());
    }

    @Test
    void testUpdateUser() {
        User user = new User();
        user.setActive(false);
        when(userRepository.save(user)).thenReturn(user);
        userService.updateUser(user);

        assertTrue(user.isActive());
    }

}
