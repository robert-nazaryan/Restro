package org.restro.security;

import lombok.Getter;
import org.restro.entity.User;
import org.springframework.security.core.authority.AuthorityUtils;


@Getter
public class SpringUser extends org.springframework.security.core.userdetails.User {

    private  User user;

    public SpringUser(User user) {
        super(user.getEmail(), user.getPassword(), user.isActive(), true, true, true,
                AuthorityUtils.createAuthorityList(user.getUserType().name()));
        this.user = user;
    }

}
