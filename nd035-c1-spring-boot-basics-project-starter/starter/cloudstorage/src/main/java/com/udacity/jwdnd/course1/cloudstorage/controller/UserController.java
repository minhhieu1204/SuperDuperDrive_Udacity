package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.entity.User;
import com.udacity.jwdnd.course1.cloudstorage.services.IUserService;
import com.udacity.jwdnd.course1.cloudstorage.utils.CommonUtil;
import com.udacity.jwdnd.course1.cloudstorage.utils.Constants;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import java.security.Principal;

@Controller
public class UserController {

    @Autowired
    private IUserService iUserService;

    @GetMapping("/login")
    public String showLogin(Model model, @RequestParam(name = "message", required = false,defaultValue = "") String message, Principal principal) {
        if(!CommonUtil.isEmpty(principal)){
            return "redirect:/home";
        }
        if (message.equals("fail")) {
            model.addAttribute(Constants.STATUS, Constants.STATUS_FAIL);
        } else if (message.equals("logout")) {
            model.addAttribute(Constants.STATUS, Constants.STATUS_SUCCESS);
        }
        return "login";
    }

    @GetMapping("/")
    public String redirectHome() {
        return "redirect:/home";
    }

    @GetMapping("/signup")
    public String showSignup() {
        return "signup";
    }

    @PostMapping("/signup")
    public String register(@ModelAttribute User user, Model model) {
        JSONObject result = iUserService.register(user);
        //model.addAttribute(Constants.STATUS, result.get(Constants.STATUS));
        if (result.get(Constants.STATUS).equals(Constants.STATUS_FAIL)) {
            model.addAttribute(Constants.MESSAGE, result.get(Constants.MESSAGE));
            return "signup";
        }
        return "redirect:/login";

    }
}
