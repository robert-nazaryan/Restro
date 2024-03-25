package org.restro.controller;

import org.restro.security.SpringUser;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@EnableAsync
@EnableScheduling
public class MainController {

    @GetMapping("/index")
    public String mainPage(@RequestParam(value = "msg", required = false) String msg, ModelMap modelMap,
                           @AuthenticationPrincipal SpringUser springUser) {
        if (msg != null && !msg.isEmpty()) {
            modelMap.addAttribute("msg", msg);
        }
        modelMap.addAttribute("currentUser", springUser.getUser());
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
