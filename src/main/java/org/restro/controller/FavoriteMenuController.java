package org.restro.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.restro.entity.FavoriteMenu;
import org.restro.entity.User;
import org.restro.exception.MenuNotFoundException;
import org.restro.security.SpringUser;
import org.restro.service.FavoriteMenuService;
import org.restro.service.MenuPictureService;
import org.restro.service.MenuService;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

@Controller
@RequestMapping("favoriteMenu")
@RequiredArgsConstructor
@Slf4j
public class FavoriteMenuController {

    private final FavoriteMenuService favoriteMenuService;
    private final MenuService menuService;
    private final MenuPictureService menuPictureService;

    @GetMapping("/{menuId}")
    public String menuSinglePage(@PathVariable("menuId") int menuId, @ModelAttribute("currentUser") User currentUser) {
        FavoriteMenu favoriteMenu = new FavoriteMenu();
        favoriteMenu.setMenu(menuService.findById(menuId)
                .orElseThrow(() -> new MenuNotFoundException("Menu not found with ID: " + menuId)));
        favoriteMenu.setUser(currentUser);
        log.info("User with id: {} add to favorites menu with id: {}", currentUser.id, favoriteMenu.getMenu().getId());
        favoriteMenuService.save(favoriteMenu);
        return "redirect:/menu";
    }

    @GetMapping(value = "/getImage", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImage(@RequestParam("picName") String picName) throws IOException {
        return menuPictureService.getMenuImage(picName);
    }

    @GetMapping("/delete/{id}")
    public String deleteMenuById(@PathVariable("id") int menuId, @AuthenticationPrincipal SpringUser springUser) {
        favoriteMenuService.deleteByMenuIdAndUserId(menuId, springUser.getUser().getId());
        return "redirect:/users/profile";
    }

}
