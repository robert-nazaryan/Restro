package org.restro.controller.admin;

import lombok.RequiredArgsConstructor;
import org.restro.entity.Menu;
import org.restro.entity.MenuPicture;
import org.restro.service.CategoryService;
import org.restro.service.MenuPictureService;
import org.restro.service.MenuService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.restro.controller.constants.AdminUrlConstants.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminMenuController {

    private final MenuService menuService;
    private final MenuPictureService menuPictureService;
    private final CategoryService categoryService;


    @GetMapping("/addMenu")
    public String addMenuPage(ModelMap modelMap) {
        modelMap.addAttribute("category", categoryService.findAllCategory());
        return ADD_MENU;
    }

    @PostMapping("/addMenu")
    public String addMenu(@ModelAttribute Menu menu,
                          @RequestParam("pics") List<MultipartFile> pics) {
        Menu saved = menuService.save(menu);
        if (Boolean.TRUE.equals(menuPictureService.savePicture(saved, pics))) {
            return REDIRECT_ADMIN_INDEX;
        }
        return "redirect:/404";
    }

    @GetMapping("/showAllMenu")
    public String showAllMenuPage(ModelMap modelMap) {
        List<MenuPicture> all = menuPictureService.findAll();
        modelMap.addAttribute("manu", menuService.findAll());
        modelMap.addAttribute("manuPicture", all);
        return SHOW_ALL_MENU;
    }

    @GetMapping("/menu/delete/{id}")
    public String deleteMenuById(@PathVariable("id") int id) {
        menuService.deleteMenuById(id);
        return REDIRECT_SHOW_ALL_MENU;
    }


    @GetMapping(value = "/getImage", produces = MediaType.IMAGE_JPEG_VALUE)
    public @ResponseBody byte[] getImage(@RequestParam("picName") String picName) throws IOException {
        return menuPictureService.getMenuImage(picName);
    }


}
