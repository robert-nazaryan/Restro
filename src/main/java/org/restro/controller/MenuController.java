package org.restro.controller;

import lombok.RequiredArgsConstructor;
import org.restro.entity.Category;
import org.restro.entity.Menu;
import org.restro.entity.MenuPicture;
import org.restro.service.CategoryService;
import org.restro.service.MenuPictureService;
import org.restro.service.MenuService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/menu")
public class MenuController {
    private final MenuService menuService;
    private final MenuPictureService menuPictureService;
    private final CategoryService categoryService;

    @GetMapping
    public String menu(ModelMap modelMap) {
        List<Menu> menus = menuService.findAll();
        List<Category> categoryList = categoryService.findAllCategory();
        List<MenuPicture> all = menuPictureService.findAll();
        modelMap.addAttribute("menu",menus);
        modelMap.addAttribute("categories",categoryList);
        modelMap.addAttribute("manuPicture", all);
        return "/menu";
    }
    @GetMapping(value = "/getImage", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImage(@RequestParam("picName") String picName) throws IOException {
        return menuPictureService.getMenuImage(picName);
    }

}
