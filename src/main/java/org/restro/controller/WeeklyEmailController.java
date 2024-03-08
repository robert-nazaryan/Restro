package org.restro.controller;

import lombok.RequiredArgsConstructor;
import org.restro.entity.WeeklyEmail;
import org.restro.service.WeeklyEmailService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/weeklyEmail")
@RequiredArgsConstructor
public class WeeklyEmailController {

    private final WeeklyEmailService weeklyEmailService;

    @PostMapping("/add")
    public String addEmail(@ModelAttribute WeeklyEmail weeklyEmail) {
        weeklyEmailService.save(weeklyEmail);
        return "redirect:/";
    }

}
