package org.restro.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CartController {

    @GetMapping("/cart-page")
    public String cartPage() {
        return "/cart-page";
    }
}
