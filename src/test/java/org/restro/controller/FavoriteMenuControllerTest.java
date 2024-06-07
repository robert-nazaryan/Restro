package org.restro.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.restro.entity.FavoriteMenu;
import org.restro.entity.Menu;
import org.restro.entity.User;
import org.restro.security.SpringUser;
import org.restro.service.FavoriteMenuService;
import org.restro.service.MenuPictureService;
import org.restro.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(FavoriteMenuController.class)
class FavoriteMenuControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FavoriteMenuService favoriteMenuService;

    @MockBean
    private MenuService menuService;

    @MockBean
    private MenuPictureService menuPictureService;

    @InjectMocks
    private FavoriteMenuController favoriteMenuController;

    @Mock
    private SpringUser springUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @WithMockUser
    void testMenuSinglePage() throws Exception {
        User currentUser = new User();
        currentUser.setId(1);
        currentUser.setEmail("poxos@gmail.com");

        Menu menu = new Menu();
        menu.setId(1);

        when(menuService.findById(1)).thenReturn(Optional.of(menu));

        mockMvc.perform(get("/favoriteMenu/1")
                        .flashAttr("currentUser", currentUser)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/menu"));

        verify(menuService, times(1)).findById(1);
        verify(favoriteMenuService, times(1)).save(any(FavoriteMenu.class));
    }


    @Test
    @WithMockUser
    void testGetImage() throws Exception {
        byte[] imageBytes = new byte[]{1, 2, 3};
        when(menuPictureService.getMenuImage("picName")).thenReturn(imageBytes);

        mockMvc.perform(get("/favoriteMenu/getImage")
                        .param("picName", "picName")
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.IMAGE_JPEG_VALUE))
                .andExpect(content().bytes(imageBytes));

        verify(menuPictureService, times(1)).getMenuImage("picName");
    }

    @Test
    void testDeleteMenuById() {
        // Create a user
        User user = new User();
        user.setId(1);
        user.setName("John");
        user.setSurname("Doe");
        user.setEmail("john@example.com");
        user.setPassword("password");
        user.setActive(true);
        user.setUserType(null);

        when(springUser.getUser()).thenReturn(user);

        assertThrows(NullPointerException.class, () -> favoriteMenuController.deleteMenuById(1, springUser));
    }
}
