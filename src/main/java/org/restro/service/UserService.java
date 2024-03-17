package org.restro.service;



import org.restro.entity.User;
import org.restro.entity.UserType;

import java.util.List;
import java.util.Optional;

public interface UserService {

    void save(User user);

    void register(User user);

    List<User> findAll();

    User findByEmail(String email);

    User findByToken(String token);

    Optional<User> byEmail(String email);

    List<User> findByUserType(UserType userType);

    void deleteUserById(int id);
    Optional<User> findById(int id);

    void updateUser(User user);

    int getUserCount();


}
