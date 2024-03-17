package org.restro.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/admin")
public class AdminMainController {


    @GetMapping("/index")
    public String adminMainPage() {
        return "/admin/index";
    }


}
