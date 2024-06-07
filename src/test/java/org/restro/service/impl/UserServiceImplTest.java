package org.restro.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.restro.entity.User;
import org.restro.entity.WeeklyEmail;
import org.restro.exception.UserNotFoundException;
import org.restro.repository.UserRepository;
import org.restro.service.SendMailService;
import org.restro.service.WeeklyEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserServiceImplTest {

    @Autowired
    private UserServiceImpl userService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private SendMailService sendMailService;

    @MockBean
    private WeeklyEmailService weeklyEmailService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    void testSaveUser() {
        User user = new User();
        user.setPassword("plainPassword");

        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        User savedUser = userService.save(user);

        assertEquals("encodedPassword", savedUser.getPassword());
        verify(userRepository, times(1)).save(user);
        verify(passwordEncoder, times(1)).encode("plainPassword");
    }

    @Test
    void testRegisterUser() {
        User user = new User();
        user.setEmail("test@example.com");

        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.register(user);

        ArgumentCaptor<WeeklyEmail> captor = ArgumentCaptor.forClass(WeeklyEmail.class);
        verify(sendMailService, times(1)).sendWelcomeMail(user);
        verify(userRepository, times(1)).save(user);
        verify(weeklyEmailService, times(1)).save(captor.capture());

        WeeklyEmail weeklyEmail = captor.getValue();
        assertEquals(user.getEmail(), weeklyEmail.getEmail());

        assertFalse(user.isActive());
        assertNotNull(user.getToken());
    }

    @Test
    void testFindAllUsers() {
        userService.findAll();
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void testFindByEmail() {
        String email = "test@example.com";
        User user = new User();
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        User foundUser = userService.findByEmail(email);

        assertEquals(email, foundUser.getEmail());
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void testFindByEmailNotFound() {
        String email = "test@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findByEmail(email));
        verify(userRepository, times(1)).findByEmail(email);
    }

    @Test
    void testFindByToken() {
        String token = UUID.randomUUID().toString();
        User user = new User();
        user.setToken(token);

        when(userRepository.findByToken(token)).thenReturn(Optional.of(user));

        User foundUser = userService.findByToken(token);

        assertEquals(token, foundUser.getToken());
        verify(userRepository, times(1)).findByToken(token);
    }

    @Test
    void testFindByTokenNotFound() {
        String token = UUID.randomUUID().toString();

        when(userRepository.findByToken(token)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findByToken(token));
        verify(userRepository, times(1)).findByToken(token);
    }

    @Test
    void testDeleteUserById() {
        int id = 1;
        userService.deleteUserById(id);
        verify(userRepository, times(1)).deleteById(id);
    }

    @Test
    void testUpdateUser() {
        User user = new User();
        user.setActive(false);

        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.updateUser(user);

        assertTrue(user.isActive());
        verify(userRepository, times(1)).save(user);
    }
}
