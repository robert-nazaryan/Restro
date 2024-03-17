package org.restro.controller.admin;

import lombok.RequiredArgsConstructor;
import org.restro.entity.Category;
import org.restro.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static org.restro.controller.constants.AdminUrlConstants.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminCategoryController {

    private final CategoryService categoryService;


    @GetMapping("/addCategory")
    public String addCategoryPage() {
        return ADD_CATEGORY;
    }

    @PostMapping("/addCategory")
    public String addCategory(@ModelAttribute Category category) {
        categoryService.saveCategory(category);
        return REDIRECT_SHOW_ALL_CATEGORY;

    }

    @GetMapping("/showAllCategory")
    public String showAllCategoryPage(ModelMap modelMap) {
        modelMap.addAttribute("category", categoryService.findAllCategory());
        return SHOW_ALL_CATEGORY;
    }

    @GetMapping("/category/delete/{id}")
    public String deleteCategoryById(@PathVariable("id") int id) {
        categoryService.deleteCategoryById(id);
        return REDIRECT_SHOW_ALL_CATEGORY;
    }

    @GetMapping("/update/{id}")
    public String updateCategory(@PathVariable("id") int id,
                                 ModelMap modelMap) {
        Optional<Category> byId = categoryService.findById(id);
        byId.ifPresent(category -> modelMap.addAttribute("categorys", category));
        return EDIT_CATEGORY;

    }

    @PostMapping("/updateCategory")
    public String updateCategory(@ModelAttribute Category category) {
        categoryService.updateCategory(category);
        return REDIRECT_SHOW_ALL_CATEGORY;
    }
}
