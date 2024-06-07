package org.restro.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.MockitoAnnotations;
import org.restro.controller.UserController;
import org.restro.controller.admin.AdminUserController;
import org.restro.entity.FavoriteMenu;
import org.restro.entity.Menu;
import org.restro.entity.User;
import org.restro.repository.FavoriteMenuRepository;
import org.restro.security.UserDetailService;
import org.restro.service.FavoriteMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class FavoriteMenuServiceImplTest {

    @Autowired
    private FavoriteMenuService favoriteMenuService;

    @MockBean
    private FavoriteMenuRepository favoriteMenuRepository;

    @MockBean
    private AdminUserController adminUserController;

    @MockBean
    private UserController userController;

    @MockBean
    private UserDetailService userDetailService;

    @MockBean
    private SendMailServiceImpl sendMailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave() {
        FavoriteMenu favoriteMenu = new FavoriteMenu();
        favoriteMenu.setId(1);

        favoriteMenuService.save(favoriteMenu);

        ArgumentCaptor<FavoriteMenu> favoriteMenuCaptor = ArgumentCaptor.forClass(FavoriteMenu.class);
        verify(favoriteMenuRepository, times(1)).save(favoriteMenuCaptor.capture());

        FavoriteMenu capturedFavoriteMenu = favoriteMenuCaptor.getValue();
        assertEquals(favoriteMenu.getId(), capturedFavoriteMenu.getId());
    }

    @Test
    void testDeleteByMenuIdAndUserId() {
        int menuId = 1;
        int userId = 1;

        favoriteMenuService.deleteByMenuIdAndUserId(menuId, userId);

        verify(favoriteMenuRepository, times(1)).deleteByMenuIdAndUserId(menuId, userId);
    }

    @Test
    void testFindById() {
        int id = 1;
        FavoriteMenu favoriteMenu = new FavoriteMenu();
        favoriteMenu.setId(id);

        when(favoriteMenuRepository.findById(id)).thenReturn(Optional.of(favoriteMenu));

        Optional<FavoriteMenu> result = favoriteMenuService.findById(id);

        assertTrue(result.isPresent());
        assertEquals(id, result.get().getId());
    }

    @Test
    void testFindAllByUserId() {
        int userId = 1;
        Pageable pageable = PageRequest.of(0, 10);
        FavoriteMenu favoriteMenu = new FavoriteMenu();
        favoriteMenu.setId(1);

        Page<FavoriteMenu> page = new PageImpl<>(Collections.singletonList(favoriteMenu));
        when(favoriteMenuRepository.findAllByUserId(userId, pageable)).thenReturn(page);

        Page<FavoriteMenu> result = favoriteMenuService.findAllByUserId(userId, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals(favoriteMenu.getId(), result.getContent().get(0).getId());
    }

    @Test
    void testMarkFavorites() {
        int userId = 1;
        Menu menu1 = new Menu();
        menu1.setId(1);
        Menu menu2 = new Menu();
        menu2.setId(2);

        User user = new User();
        user.setId(userId);

        FavoriteMenu favoriteMenu = new FavoriteMenu();
        favoriteMenu.setUser(user);
        favoriteMenu.setMenu(menu1);

        when(favoriteMenuRepository.findAll()).thenReturn(Collections.singletonList(favoriteMenu));

        List<Menu> result = favoriteMenuService.markFavorites(userId, List.of(menu1, menu2));

        assertTrue(result.get(0).isFavorite());
        assertFalse(result.get(1).isFavorite());
    }
}
