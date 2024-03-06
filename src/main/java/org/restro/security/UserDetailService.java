package org.restro.security;

import lombok.RequiredArgsConstructor;
import org.restro.entity.User;
import org.restro.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailService implements UserDetailsService {

    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User byEmail = userService.findByEmail(username);
        if (byEmail == null) {
            throw new UsernameNotFoundException("User with username " + username + "does not exist");
        }
        return new SpringUser(byEmail);
    }

}
