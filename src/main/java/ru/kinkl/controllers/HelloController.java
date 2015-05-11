package ru.kinkl.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

@Controller
@RequestMapping("/")
public class HelloController {
    @RequestMapping(method = RequestMethod.GET)
    public String getHome(ModelMap map) {
        map.put("message", "Hello World!");
        return "home";
    }

    @RequestMapping(method = RequestMethod.GET, value = "login")
    public String getLogin() {
        return "login";
    }
}