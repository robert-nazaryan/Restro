package org.restro.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/index")
    public String mainPage() {
        return "/index";
    }

    @GetMapping("/index-2")
    public String mainPage2() {
        return "/index-2";
    }












}
