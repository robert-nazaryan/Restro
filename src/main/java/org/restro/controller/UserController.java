package org.restro.controller;

import lombok.RequiredArgsConstructor;
import org.restro.entity.User;
import org.restro.entity.UserType;
import org.restro.security.SpringUser;
import org.restro.service.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/verify")
    public String verifyUser(@RequestParam("token") String token) {
        User byToken = userService.findByToken(token);
        if (byToken == null) {
            return "redirect:/";
        }
        byToken.setActive(true);
        byToken.setToken(null);
        userService.save(byToken);
        return "redirect:/";
    }

    @PostMapping("/register")
    public String userRegister(@ModelAttribute User user) {
        User byEmail = userService.findByEmail(user.getEmail());
        if (byEmail == null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setUserType(UserType.USER);
            userService.register(user);
            return "redirect:/?msg=Check your email for activate account";
        }
        return "redirect:/?msg=Email already in use";
    }

    @GetMapping("/loginSuccess")
    public String loginSuccess(@AuthenticationPrincipal SpringUser springUser) {
        if (springUser.getUser().getUserType() == UserType.ADMIN) {
            return "redirect:/admin/index";
        }
        return "redirect:/";
    }

}
