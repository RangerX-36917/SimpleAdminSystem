package com.cs316.SimpleAdminSys.web;

import com.cs316.SimpleAdminSys.api.UserRepository;
import com.cs316.SimpleAdminSys.model.User;
import com.cs316.SimpleAdminSys.service.WebSecurityConfig;
import com.cs316.SimpleAdminSys.service.auth.LoginService;
import com.cs316.SimpleAdminSys.service.auth.PasswordAuthentication;
import com.cs316.SimpleAdminSys.service.auth.SignupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class UserController {
    @Autowired
    private LoginService loginService;
    @Autowired
    private SignupService signupService;
    @Autowired
    private UserRepository userDao;

    @RequestMapping(value = "/userLogin", method = RequestMethod.POST)
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession httpSession,
                        Model model) {

        User tempuser = new User();
        tempuser.setPassword(password);
        tempuser.setUsername(username);

        boolean verify = loginService.verifyLogin(tempuser);
        if (verify) {
            httpSession.setAttribute(WebSecurityConfig.SESSION_KEY, username);
            User user = userDao.findUserByUsername(username);
            httpSession.setAttribute("user", user);
            if (user.getType() == User.userType.ADMIN) {
                return "redirect:/adminHome";
            } else
                return "redirect:/userHome";
        } else {
            model.addAttribute("message", "Invalid Credential");

            return "login";
        }

    }

    @GetMapping(value = {"/login", "/login.html"})
    public String signinControl() {
        return "login";
    }

    @GetMapping(value = {"/signup", "/signup.html"})
    public String signupControl() {
        return "signup";
    }

    @GetMapping(value = {"/logout", "/logout.html"})
    public String logout(HttpSession session) {
        session.removeAttribute(WebSecurityConfig.SESSION_KEY);
        return "redirect:/index";
    }

    @PostMapping(value = {"/userSignup"})
    public String signup(@RequestParam String fullName,
                         @RequestParam String email,
                         @RequestParam String userName,
                         @RequestParam String password,
                         @RequestParam String phoneNum,
                         @RequestParam String organization,
                         @RequestParam String type,
                         HttpSession httpSession,
                         HttpServletRequest httpServletRequest,
                         Model model) {

        User user = setUser(fullName, email, userName, password, phoneNum, organization, type);
        model.addAttribute("user", user);
        try {
            signupService.signupUser(user);
            httpSession.setAttribute(WebSecurityConfig.SESSION_KEY, userName);
            httpSession.setAttribute("user", userDao.findUserByUsername(userName));
            if (user.getType() == User.userType.ADMIN)
                return "redirect:/adminHome";
            else
                return "redirect:/userHome";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("message", "Username already exists!");
            return "signup";
        }
    }

    private User setUser(@RequestParam String fullName, @RequestParam String email, @RequestParam String userName, @RequestParam String password, @RequestParam String phoneNum, @RequestParam String organization, @RequestParam String type) {
        User user = new User();
        user.setFullName(fullName);
        user.setUsername(userName);
        user.setPassword(password);
        user.setMail(email);
        user.setPhone(phoneNum);
        user.setOrganization(organization);
        user.setType(User.userType.valueOf(type));
        return user;
    }

    @PostMapping(value = {"/update"})
    public String update(@RequestParam String fullName,
                         @RequestParam String email,
                         @RequestParam String username,
                         @RequestParam String phoneNum,
                         HttpSession httpSession,
                         HttpServletRequest httpServletRequest,
                         Model model) {
        User user = (User) httpSession.getAttribute("user");
        user.setFullName(fullName);
        user.setMail(email);
        user.setUsername(username);
        user.setPhone(phoneNum);
        model.addAttribute("user", user);
        try {
            userDao.saveAndFlush(user);
        } catch (Exception e) {
            model.addAttribute("message", "Username already exists! Update failed.");
            return "update";
        }
        return "/myProfile";
    }

    @PostMapping(value = {"/createUser"})
    public String create(@RequestParam String fullName,
                         @RequestParam String email,
                         @RequestParam String userName,
                         @RequestParam String password,
                         @RequestParam String phoneNum,
                         @RequestParam String type,
                         HttpSession httpSession,
                         Model model) {
        String organization = ((User) httpSession.getAttribute("user")).getOrganization();
        User user = setUser(fullName, email, userName, password, phoneNum, organization, type);
        try {
            signupService.signupUser(user);
            return "redirect:/adminHome";
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("message", "Username already exists!");
            return "createUser";
        }
    }

    @PostMapping(value = {"/changePassword"})
    public String change(@RequestParam String oldPassword,
                         @RequestParam String newPassword,
                         @RequestParam String reNewPassword,
                         HttpSession httpSession,
                         Model model) {
        User user = (User) httpSession.getAttribute("user");
        PasswordAuthentication passwordAuthentication = new PasswordAuthentication();
        String correctPassword = userDao.findUserByUsername(user.getUsername()).getPassword();
        if (!passwordAuthentication.authenticate(oldPassword.toCharArray(), correctPassword)) {
            model.addAttribute("message", "Incorrect old password ");
            return "changePassword";
        }
        if (newPassword.equals(reNewPassword)) {
            user.setPassword(passwordAuthentication.hash(newPassword));
            try {
                userDao.saveAndFlush(user);
            } catch (Exception e) {
                e.printStackTrace();
                model.addAttribute("message", "Change failed. Please try again. ");
                return "changePassword";
            }
        } else {
            model.addAttribute("message", "Two new passwords isn't the same. Please try again. ");
            return "changePassword";
        }
        return "redirect:/myProfile";


    }

}
