package ru.kinkl.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.kinkl.dto.CredentialsDto;
import ru.kinkl.dto.UserDto;
import ru.kinkl.model.Authority;
import ru.kinkl.model.User;
import ru.kinkl.persistence.AuthorityDao;
import ru.kinkl.persistence.UserDao;

import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.*;

@RestController
@RequestMapping("/")
public class HomeController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private AuthorityDao authorityDao;

    @Autowired
    @Qualifier("authenticationManager")
    private AuthenticationManager authenticationManager;

    @RequestMapping(value = "/authenticated_user", method = RequestMethod.GET)
    public @ResponseBody UserDto getAuthenticatedUser(Principal principal) {
        if (principal != null) {
            UserDto user = new UserDto();
            user.setName(principal.getName());
            return user;
        }
        return null;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST, headers = {"Content-Type=application/json"})
    public void login(@RequestBody CredentialsDto credentials, HttpServletResponse response) {
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

    @RequestMapping(value = "/is_user_exists", method = RequestMethod.GET)
    public Map<String, Object> isUserExists(@RequestParam("username") String username) {
        Map<String, Object> map = new HashMap<String, Object>();
        User user = userDao.findByNameIgnoreCase(username);
        if (user != null) {
            map.put("isUserExists", username.toUpperCase().equals("ADMIN") || username.toUpperCase().equals("USER"));
        }
        return map;
    }

    @RequestMapping(value = "/sign_on", method = RequestMethod.POST, headers = {"Content-Type=application/json"})
    public void signOn(@RequestBody CredentialsDto credentials, HttpServletResponse response) {
        User user = userDao.findByNameIgnoreCase(credentials.getUsername());
        if (user != null) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }
        User newUser = new User();
        newUser.setName(credentials.getUsername());
        newUser.setPassword(credentials.getPassword());
        newUser.setEnabled(true);
        userDao.save(newUser);

        Authority authority = new Authority();
        authority.setAuthority("ROLE_USER");
        authority.setUser(newUser);
        authorityDao.save(authority);

        response.setStatus(HttpServletResponse.SC_OK);
    }

    @RequestMapping("random_data")
    public Map<String, Object> randomData() {
        Map<String, Object> model = new HashMap<String, Object>();
        model.put("id", UUID.randomUUID().toString());
        model.put("content", "Hello World");
        return model;
    }
}
