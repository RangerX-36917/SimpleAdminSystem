package com.ooad.scriptpro.web;

import com.ooad.scriptpro.api.UserRepository;
import com.ooad.scriptpro.model.User;
import com.ooad.scriptpro.service.WebSecurityConfig;
import com.ooad.scriptpro.service.auth.LoginService;
import com.ooad.scriptpro.service.auth.SignupService;
import javassist.tools.web.Webserver;
import org.hibernate.usertype.UserType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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

//    @GetMapping("/")
//    public String index(Model model, HttpSession httpSession){
//        if(httpSession.getAttribute(WebSecurityConfig.SESSION_KEY) == null){
//            model.addAttribute("message","");
//            return "login";
//        }
//        return "redirect:/userHome";
//    }

    @RequestMapping(value = "/userLogin", method = RequestMethod.POST)
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession httpSession,
                        Model model){
        //System.out.println("call log in");
        //System.out.println(username+"  "+password);
        User tempuser = new User();
        tempuser.setPassword(password);
        tempuser.setUsername(username);

        //System.out.println("username: "+username+"  password: "+password);
        boolean verify = loginService.verifyLogin(tempuser);
        if(verify) {
            System.out.println("success");
            httpSession.setAttribute(WebSecurityConfig.SESSION_KEY, username);
            User user = userDao.findUserByUsername(username);
            httpSession.setAttribute("user", user);
            if(user.getType() == User.userType.ADMIN) {
                System.out.println("is admin");
                return "redirect:/adminHome";
            }
            else
                return "redirect:/userHome";
        } else{
            System.out.println("fail");
            model.addAttribute("message","Invalid Credential");

            return "login";
        }

    }

    @GetMapping(value = {"/login","/login.html"})
    public String signinControl(){
        //model.addAttribute("message","")
        return "login";
    }

    @GetMapping(value={"/signup","/signup.html"})
    public String signupControl(){
        return "signup";
    }
    @GetMapping(value = {"/logout","/logout.html"})
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
                         Model model){
        System.out.println(fullName);
        System.out.println(email);
        System.out.println(userName);
        System.out.println(password);
        User user = setUser(fullName, email, userName, password, phoneNum, organization, type);
        model.addAttribute("user", user);
        System.out.println(" sign up user, username: "+user.getUsername()+"  password: "+user.getPassword());
        try{
            signupService.signupUser(user);
            System.out.println("success");
            httpSession.setAttribute(WebSecurityConfig.SESSION_KEY, userName);
            httpSession.setAttribute("user",userDao.findUserByUsername(userName));
            if(user.getType() == User.userType.ADMIN)
                return "redirect:/adminHome";
            else
                return "redirect:/userHome";
        } catch (Exception e){
            System.out.println("fail");
            e.printStackTrace();
            model.addAttribute("message","Username already exists!");
            return "signup";
        }
        /*
        if(signupService.signupUser(user)){
            System.out.println("success");
            httpSession.setAttribute(WebSecurityConfig.SESSION_KEY, userName);
            return "/userHome";
        } else{
            System.out.println("fail");
            return "/login";
        }
        */
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
        User user = (User)httpSession.getAttribute("user");
        user.setFullName(fullName);
        user.setMail(email);
        user.setUsername(username);
        user.setPhone(phoneNum);
        model.addAttribute("user", user);
        try{
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
                         @RequestParam String organization,
                         @RequestParam String type,
                         Model model){
        User user = setUser(fullName, email, userName, password, phoneNum, organization, type);
        try{
            signupService.signupUser(user);
            return "redirect:/adminHome";
        } catch (Exception e){
            e.printStackTrace();
            model.addAttribute("message","Username already exists!");
            return "createUser";
        }
        /*
        if(signupService.signupUser(user)){
            System.out.println("success");
            httpSession.setAttribute(WebSecurityConfig.SESSION_KEY, userName);
            return "/userHome";
        } else{
            System.out.println("fail");
            return "/login";
        }
        */
    }
}
