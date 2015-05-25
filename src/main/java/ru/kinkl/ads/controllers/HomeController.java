package ru.kinkl.ads.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.kinkl.ads.dto.CredentialsDto;
import ru.kinkl.ads.dto.UserDto;
import ru.kinkl.ads.model.Authority;
import ru.kinkl.ads.model.User;
import ru.kinkl.ads.persistence.AuthorityDao;
import ru.kinkl.ads.persistence.UserDao;

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
            User user = userDao.findByNameIgnoreCase(principal.getName());
            UserDto dto = new UserDto();
            dto.setName(principal.getName());
            dto.setIsAdmin(false);
            List<Authority> authorities = user.getAuthority();
            for (Authority authority : authorities) {
                if (authority.getAuthority().equals("ROLE_ADMIN")) {
                    dto.setIsAdmin(true);
                }
            }
            return dto;
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
        map.put("isUserExists", user != null);
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

    @RequestMapping(value = "/admin_action_stub", method = RequestMethod.POST)
    @ResponseStatus(HttpStatus.OK)
    public void adminActionStub() { }
}
