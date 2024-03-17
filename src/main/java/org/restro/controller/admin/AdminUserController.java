package org.restro.controller.admin;

import lombok.RequiredArgsConstructor;
import org.restro.entity.User;
import org.restro.entity.UserType;
import org.restro.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static org.restro.controller.constants.AdminUrlConstants.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminUserController {

    private final UserService userService;

    @GetMapping("/addUser")
    public String addUser() {
        return ADD_USER;
    }

    @PostMapping("/addUser")
    public String addUser(@ModelAttribute User user, ModelMap modelMap) {
        Optional<User> userByEmail = userService.byEmail(user.getEmail());
        if (userByEmail.isPresent()) {
            modelMap.addAttribute("errorMassage", "Email already in use");
            return ADD_USER;
        }
        userService.save(user);
        return REDIRECT_SHOW_ALL_USER;
    }

    @GetMapping("/showAllUser")
    public String adminShowAllUser(ModelMap modelMap) {
        modelMap.addAttribute("users", userService.findByUserType(UserType.USER));
        return SHOW_ALL_USER;
    }


    @GetMapping("/user/delete/{id}")
    public String deleteUserById(@PathVariable("id") int id) {
        userService.deleteUserById(id);
        return REDIRECT_SHOW_ALL_USER;
    }

    @GetMapping("/updateUser/{id}")
    public String updateUser(@PathVariable("id") int id,
                             ModelMap modelMap) {
        Optional<User> byId = userService.findById(id);
        byId.ifPresent(user -> modelMap.addAttribute("user", user));
        return EDIT_USER;

    }

    @PostMapping("/updateUser")
    public String updateUser(@ModelAttribute User user) {
        userService.updateUser(user);
        return REDIRECT_SHOW_ALL_USER;
    }
}
