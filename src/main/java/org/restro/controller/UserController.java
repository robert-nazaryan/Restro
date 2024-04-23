package org.restro.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.restro.controller.constants.AdminUrlConstants;
import org.restro.entity.MenuPicture;
import org.restro.entity.User;
import org.restro.entity.UserType;
import org.restro.service.FavoriteMenuService;
import org.restro.service.MenuPictureService;
import org.restro.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final FavoriteMenuService favoriteMenuService;
    private final MenuPictureService menuPictureService;

    @GetMapping("/verify")
    public String verifyUser(@RequestParam("token") String token) {
        User byToken = userService.findByToken(token);
        if (byToken != null) {
            byToken.setActive(true);
            byToken.setToken(null);
            userService.save(byToken);
            log.info("User with email {} verified", byToken.getEmail());
        }
        return "redirect:/";
    }

    @PostMapping("/register")
    public String userRegister(@ModelAttribute User user) {
        User byEmail = userService.findByEmail(user.getEmail());
        if (byEmail == null) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setUserType(UserType.USER);
            userService.register(user);
            log.info("User with email {} registered", user.getEmail());
            return "redirect:/?msg=Check your email for activate account";
        }
        log.info("User with email {} already registered", user.getEmail());
        return "redirect:/?msg=Email already in use";
    }

    @GetMapping("/loginSuccess")
    public String loginSuccess(@ModelAttribute("currentUser") User currentUser) {
        if (currentUser.getUserType() == UserType.ADMIN) {
            log.info("Admin with email {} logged in", currentUser.getEmail());
            return AdminUrlConstants.REDIRECT_ADMIN_INDEX;
        }
        log.info("User with email {} logged in", currentUser.getEmail());
        return "redirect:/index";
    }

    @GetMapping("/profile")
    public String profilePage(@ModelAttribute("currentUser") User currentUser, ModelMap modelMap) {
        List<MenuPicture> menuPictures = menuPictureService.findAll();
        modelMap.addAttribute("currentUser", currentUser);
        modelMap.addAttribute("favorites", favoriteMenuService.findAllByUserId(currentUser.id));
        modelMap.addAttribute("manuPictures", menuPictures);
        return "/user-profile";
    }

    @GetMapping("/profile/edit")
    public String editProfilePage(@ModelAttribute("currentUser") User currentUser, ModelMap modelMap) {
        modelMap.addAttribute("currentUser", currentUser);
        return "/user-profile-edit";
    }

    @PostMapping("/profile/edit")
    public String editProfile(@ModelAttribute User user) {
        userService.updateUser(user);
        return "redirect:/users/profile";
    }

}
