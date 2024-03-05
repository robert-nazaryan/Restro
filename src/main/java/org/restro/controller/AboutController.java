package org.restro.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AboutController {

    @GetMapping("/about-page")
    public String about() {
        return "/about-page";
    }
}
