package org.restro.controller;

import org.restro.entity.User;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@EnableAsync
@EnableScheduling
public class MainController {

    @GetMapping("/index")
    public String mainPage(@RequestParam(value = "msg", required = false) String msg, ModelMap modelMap,
                           @ModelAttribute("currentUser") User currentUser) {
        if (msg != null && !msg.isEmpty()) {
            modelMap.addAttribute("msg", msg);
        }
        modelMap.addAttribute("currentUser", currentUser);
        return "/index";
    }

    @GetMapping("/index-2")
    public String mainPage2(@RequestParam(value = "msg", required = false) String msg, ModelMap modelMap) {
        if (msg != null && !msg.isEmpty()) {
            modelMap.addAttribute("msg", msg);
        }
        return "/index-2";
    }


}
