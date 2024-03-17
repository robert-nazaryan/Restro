package org.restro.repository;

import org.restro.entity.User;
import org.restro.entity.UserType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    Optional<User> findByEmail(String email);

    Optional<User> findByToken(String token);

    List<User> findAllByUserType(UserType userType);

}
