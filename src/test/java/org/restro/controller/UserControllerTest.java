package org.restro.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.restro.controller.constants.AdminUrlConstants;
import org.restro.entity.MenuPicture;
import org.restro.entity.User;
import org.restro.entity.UserType;
import org.restro.service.FavoriteMenuService;
import org.restro.service.MenuPictureService;
import org.restro.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.ModelMap;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private FavoriteMenuService favoriteMenuService;

    @Mock
    private MenuPictureService menuPictureService;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testVerifyUser() {
        User user = new User();
        user.setEmail("poxos@gmail.com");
        user.setActive(false);
        when(userService.findByToken("token")).thenReturn(user);
        String result = userController.verifyUser("token");

        assertEquals("redirect:/", result);
        assertTrue(user.isActive());
        assertNull(user.getToken());
        verify(userService, times(1)).save(user);
    }

    @Test
    void testUserRegisterNewUser() {
        User user = new User();
        user.setEmail("poxos@gmail.com");
        user.setPassword("password");
        when(userService.findByEmail("poxos@gmail.com")).thenReturn(null);
        String result = userController.userRegister(user);

        assertEquals("redirect:/?msg=Check your email for activate account", result);
        verify(userService, times(1)).register(user);
    }

    @Test
    void testUserRegisterExistingUser() {
        User user = new User();
        user.setEmail("poxos@gmail.com");
        when(userService.findByEmail("poxos@gmail.com")).thenReturn(user);
        String result = userController.userRegister(user);

        assertEquals("redirect:/?msg=Email already in use", result);
        verify(userService, never()).register(user);
    }

    @Test
    void testLoginSuccessAdminUser() {
        User currentUser = new User();
        currentUser.setEmail("poxos@gmail.com");
        currentUser.setUserType(UserType.ADMIN);
        String result = userController.loginSuccess(currentUser);

        assertEquals(AdminUrlConstants.REDIRECT_ADMIN_INDEX, result);
    }

    @Test
    void testLoginSuccessNormalUser() {
        User currentUser = new User();
        currentUser.setEmail("poxos@gmail.com");
        currentUser.setUserType(UserType.USER);
        String result = userController.loginSuccess(currentUser);

        assertEquals("redirect:/index", result);
    }

    @Test
    void testProfilePage() {
        User currentUser = new User();
        currentUser.setEmail("poxos@gmail.com");
        currentUser.setId(1);
        List<MenuPicture> menuPictures = new ArrayList<>();
        when(menuPictureService.findAll()).thenReturn(menuPictures);
        when(favoriteMenuService.findAllByUserId(1)).thenReturn(new ArrayList<>());
        ModelMap modelMap = new ModelMap();
        String result = userController.profilePage(currentUser, modelMap);

        assertEquals("/user-profile", result);
        assertEquals(currentUser, modelMap.get("currentUser"));
        assertEquals(menuPictures, modelMap.get("manuPictures"));
        verify(favoriteMenuService, times(1)).findAllByUserId(1);
    }

    @Test
    void testEditProfilePage() {
        User currentUser = new User();
        currentUser.setEmail("poxos@gmail.com");
        ModelMap modelMap = new ModelMap();
        String result = userController.editProfilePage(currentUser, modelMap);

        assertEquals("/user-profile-edit", result);
        assertEquals(currentUser, modelMap.get("currentUser"));
    }

    @Test
    void testEditProfile() {
        User user = new User();
        user.setEmail("poxos@gmail.com");
        String result = userController.editProfile(user);

        assertEquals("redirect:/users/profile", result);
        verify(userService, times(1)).updateUser(user);
    }

}