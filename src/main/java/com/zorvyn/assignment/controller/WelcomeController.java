package com.zorvyn.assignment.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class WelcomeController {

    @GetMapping("/")
    public RedirectView root() {
        return new RedirectView("/signup.html");
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        String email = SecurityContextHolder.getContext()
                .getAuthentication()
                .getName();
        return "Hello, " + email + "! You are authenticated.";
    }
}