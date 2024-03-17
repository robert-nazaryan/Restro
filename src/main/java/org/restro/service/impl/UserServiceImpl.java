package org.restro.service.impl;

import lombok.RequiredArgsConstructor;
import org.restro.entity.User;
import org.restro.entity.UserType;
import org.restro.repository.UserRepository;
import org.restro.service.SendMailService;
import org.restro.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final SendMailService sendMailService;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.password));
        userRepository.save(user);
    }

    @Override
    public void register(User user) {
        String activationToken = UUID.randomUUID().toString();
        user.setActive(false);
        user.setToken(activationToken);
        sendMailService.sendWelcomeMail(user);
        userRepository.save(user);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public User findByToken(String token) {
        return userRepository.findByToken(token).orElse(null);
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
        userRepository.save(user);
    }

    @Override
    public int getUserCount() {
        return userRepository.findAll().size();
    }
}
