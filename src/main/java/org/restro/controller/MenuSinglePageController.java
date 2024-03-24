package org.restro.controller;

import lombok.RequiredArgsConstructor;
import org.restro.entity.Menu;
import org.restro.entity.MenuPicture;
import org.restro.service.MenuPictureService;
import org.restro.service.MenuService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MenuSinglePageController {
    private final MenuService menuService;
    private final MenuPictureService menuPictureService;

    @GetMapping("/menu/{id}")
    public String menuSinglePage(@PathVariable("id") int id, ModelMap modelMap) {
        List<MenuPicture> menuPictures = menuPictureService.findAll();
        Optional<Menu> byId = menuService.findById(id);
        if (byId.isEmpty()) {
            return "redirect:/";
        }
        modelMap.addAttribute("menusPic", menuPictures);
        modelMap.addAttribute("menuPage", byId.get());
        return "menu-single";
    }

    @GetMapping(value = "/getImage", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImage(@RequestParam("picName") String picName) throws IOException {
        return menuPictureService.getMenuImage(picName);
    }
}
