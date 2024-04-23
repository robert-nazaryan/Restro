package org.restro.controller;

import lombok.extern.slf4j.Slf4j;
import org.restro.exception.MenuNotFoundException;
import org.restro.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @Value("${error.page.path}")
    private String errorPagePath;

    @Value("${notfound.page.path}")
    private String notFoundPagePath;

    @RequestMapping("/404")
    public String handle404() {
        return errorPagePath;
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ModelAndView handleUserNotFoundException(UserNotFoundException ex) {
        log.error("User not found", ex);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("errorMessage", ex.getMessage());
        modelAndView.setViewName(notFoundPagePath);
        return modelAndView;
    }

    @ExceptionHandler(MenuNotFoundException.class)
    public ModelAndView handleMenuNotFoundException(MenuNotFoundException ex) {
        log.error("Menu not found", ex);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("errorMessage", ex.getMessage());
        modelAndView.setViewName(notFoundPagePath);
        return modelAndView;
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleException(Exception ex) {
        log.error("An unexpected error occurred", ex);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName(errorPagePath);
        return modelAndView;
    }

}
