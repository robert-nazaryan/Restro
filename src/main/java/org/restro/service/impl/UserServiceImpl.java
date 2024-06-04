package org.restro.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.restro.entity.User;
import org.restro.entity.UserType;
import org.restro.entity.WeeklyEmail;
import org.restro.exception.UserNotFoundException;
import org.restro.repository.UserRepository;
import org.restro.service.SendMailService;
import org.restro.service.UserService;
import org.restro.service.WeeklyEmailService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final SendMailService sendMailService;
    private final WeeklyEmailService weeklyEmailService;


    private final PasswordEncoder passwordEncoder;

    @Override
    public User save(User user) {
        user.setPassword(passwordEncoder.encode(user.password));
        userRepository.save(user);
        log.info("User saved: {}", user);
        return user;
    }

    @Override
    public void register(User user) {
        String activationToken = UUID.randomUUID().toString();
        user.setActive(false);
        user.setToken(activationToken);
        sendMailService.sendWelcomeMail(user);
        userRepository.save(user);
        log.info("User registered: {}", user);
        WeeklyEmail weeklyEmail = new WeeklyEmail();
        weeklyEmail.setEmail(user.getEmail());
        weeklyEmailService.save(weeklyEmail);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
    }

    @Override
    public User findByToken(String token) {
        return userRepository.findByToken(token).orElseThrow(() -> new UserNotFoundException("User not found with token: " + token));
    }

    @Override
    public Optional<User> byEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> findByUserType(UserType userType) {
        return userRepository.findAllByUserType(UserType.USER);
    }

    @Override
    public void deleteUserById(int id) {
        userRepository.deleteById(id);
    }

    @Override
    public Optional<User> findById(int id) {
        return userRepository.findById(id);
    }

    @Override
    public void updateUser(User user) {
        user.setActive(true);
        userRepository.save(user);
        log.info("User updated: {}", user);
    }

}
