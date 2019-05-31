package com.cs316.SimpleAdminSys.web;

import com.cs316.SimpleAdminSys.model.User;
import com.cs316.SimpleAdminSys.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class PageController {
    @Autowired
    UserService userService;

    @GetMapping(value = {"/adminHome"})
    public String adminHomeController(Model model,
                                      HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        List<User> users = userService.getUsersByOrg(user.getOrganization());
        model.addAttribute("userList", users);
        model.addAttribute("user", user);
        return "adminHome";
    }

    @GetMapping(value = {"/userHome"})
    public String userHomeController(Model model,
                                     HttpSession httpSession) {
        User user = (User) httpSession.getAttribute("user");
        model.addAttribute("user", user);
        if (user.getType() == User.userType.ADMIN) {
            return "redirect:/adminHome";
        } else
            return "redirect:/userHome";
    }

    @GetMapping(value = {"/createUser"})
    public String createUserController(Model model,
                                       HttpSession httpSession) {
        return "createUser";
    }
}
