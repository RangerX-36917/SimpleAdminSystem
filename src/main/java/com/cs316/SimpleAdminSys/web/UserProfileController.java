package com.cs316.SimpleAdminSys.web;

import com.cs316.SimpleAdminSys.model.User;
import com.cs316.SimpleAdminSys.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class UserProfileController {
    @Autowired
    UserService userService;

    @GetMapping(value = "/myProfile")
    public String profileController(Model model,
                                    HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        model.addAttribute("user", user);
        return "myProfile";
    }

    @GetMapping(value = "/adminHome/delete")
    public String deleteScriptController(@RequestParam long uid) {
        userService.deleteUserByID(uid);
        return "redirect:/adminHome";
    }

    @GetMapping(value = "/adminHome/resetPassword")
    public String resetPasswordController(@RequestParam long uid) {
        userService.resetPasswordByID(uid);
        return "redirect:/adminHome";
    }

    @GetMapping(value = {"/update"})
    public String update(HttpSession httpSession,
                         Model model) {
        model.addAttribute("user", httpSession.getAttribute("user"));
        return "update";
    }

    @GetMapping(value = {"/changePassword"})
    public String change(HttpSession httpSession,
                         Model model) {
        model.addAttribute("user", httpSession.getAttribute("user"));
        return "changePassword";
    }
}
