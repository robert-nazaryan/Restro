package org.restro.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.restro.controller.constants.AdminUrlConstants;
import org.restro.entity.FavoriteMenu;
import org.restro.entity.MenuPicture;
import org.restro.entity.User;
import org.restro.entity.UserType;
import org.restro.exception.UserNotFoundException;
import org.restro.service.FavoriteMenuService;
import org.restro.service.MenuPictureService;
import org.restro.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
            userService.updateUser(byToken);
            log.info("User with email {} verified", byToken.getEmail());
        }
        return "redirect:/";
    }

    @PostMapping("/register")
    public String userRegister(@ModelAttribute User user) {
        try {
            userService.findByEmail(user.getEmail());
        } catch (UserNotFoundException e) {
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
    public String profilePage(@ModelAttribute("currentUser") User currentUser, ModelMap modelMap,
                              @RequestParam(value = "page", defaultValue = "1", required = false) int page,
                              @RequestParam(value = "size", defaultValue = "3", required = false) int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        List<MenuPicture> menuPictures = menuPictureService.findAll();
        Page<FavoriteMenu> allByUserId = favoriteMenuService.findAllByUserId(currentUser.id, pageable);

        modelMap.addAttribute("currentUser", currentUser);
        modelMap.addAttribute("favorites", allByUserId);
        modelMap.addAttribute("manuPictures", menuPictures);

        int totalPages = 0;
        if (allByUserId != null) {
            totalPages = allByUserId.getTotalPages();
        }
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            modelMap.addAttribute("pageNumbers", pageNumbers);
        }

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
