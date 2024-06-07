package org.restro.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.restro.controller.constants.AdminUrlConstants;
import org.restro.entity.FavoriteMenu;
import org.restro.entity.MenuPicture;
import org.restro.entity.User;
import org.restro.entity.UserType;
import org.restro.exception.UserNotFoundException;
import org.restro.service.FavoriteMenuService;
import org.restro.service.MenuPictureService;
import org.restro.service.SendMailService;
import org.restro.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.ModelMap;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserControllerTest {

    @Autowired
    private UserController userController;

    @MockBean
    private UserService userService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private FavoriteMenuService favoriteMenuService;

    @MockBean
    private MenuPictureService menuPictureService;

    @MockBean
    private SendMailService sendMailService;

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
        verify(userService, times(1)).updateUser(user);
    }

    @Test
    void testUserRegisterNewUser() {
        User user = new User();
        user.setEmail("poxos@gmail.com");
        user.setPassword("password");

        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        doThrow(new UserNotFoundException("User not found")).when(userService).findByEmail("poxos@gmail.com");

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
        List<FavoriteMenu> favoriteMenus = new ArrayList<>();
        Pageable pageable = PageRequest.of(0, 3);
        Page<FavoriteMenu> favoriteMenuPage = new PageImpl<>(favoriteMenus, pageable, favoriteMenus.size());

        when(menuPictureService.findAll()).thenReturn(menuPictures);
        when(favoriteMenuService.findAllByUserId(1, pageable)).thenReturn(favoriteMenuPage);

        ModelMap modelMap = new ModelMap();
        String result = userController.profilePage(currentUser, modelMap, 1, 3);

        assertEquals("/user-profile", result);
        assertEquals(currentUser, modelMap.get("currentUser"));
        assertEquals(favoriteMenuPage, modelMap.get("favorites"));
        assertEquals(menuPictures, modelMap.get("manuPictures"));

        int totalPages = favoriteMenuPage.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            assertEquals(pageNumbers, modelMap.get("pageNumbers"));
        }

        verify(favoriteMenuService, times(1)).findAllByUserId(1, pageable);
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
