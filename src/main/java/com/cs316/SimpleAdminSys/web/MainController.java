package com.cs316.SimpleAdminSys.web;

import com.cs316.SimpleAdminSys.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @Autowired
    UserService userService;

    @GetMapping("/")
    public String welcomeControl() {
        return "index";
    }

    @GetMapping(value = {"/index", "/welcome", "/welcome.html"})
    public String infoControl() {
        return "index";
    }

}
