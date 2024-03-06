package org.restro.service;


import org.restro.entity.User;

import java.util.List;

public interface UserService {

    void save(User user);

    void register(User user);

    List<User> findAll();

    User findByEmail(String email);

    User findByToken(String token);

}
