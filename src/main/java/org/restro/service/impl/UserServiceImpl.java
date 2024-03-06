package org.restro.service.impl;

import lombok.RequiredArgsConstructor;
import org.restro.entity.User;
import org.restro.repository.UserRepository;
import org.restro.service.SendMailService;
import org.restro.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final SendMailService sendMailService;

    @Override
    public void save(User user) {
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

}
