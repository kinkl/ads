package ru.kinkl.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.kinkl.dto.Credentials;
import ru.kinkl.dto.User;

import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class HomeController {

    @Autowired
    @Qualifier("authenticationManager")
    AuthenticationManager authenticationManager;

    @RequestMapping(value = "/authenticated_user", method = RequestMethod.GET)
    public @ResponseBody User getAuthenticatedUser(Principal principal) {
        if (principal != null) {
            User user = new User();
            user.setName(principal.getName());
            return user;
        }
        return null;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST, headers = {"Content-Type=application/json"})
    public void login(@RequestBody Credentials credentials, HttpServletResponse response) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword());
        try {
            Authentication auth = authenticationManager.authenticate(token);
            SecurityContextHolder.getContext().setAuthentication(auth);
            response.setStatus(HttpServletResponse.SC_OK);
        }
        catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    @RequestMapping("random_data")
    public Map<String, Object> randomData() {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("id", UUID.randomUUID().toString());
        model.put("content", "Hello World");
        return model;
    }
}
