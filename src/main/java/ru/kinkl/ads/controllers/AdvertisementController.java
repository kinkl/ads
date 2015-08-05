package ru.kinkl.ads.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.kinkl.ads.dto.AdvertisementDto;
import ru.kinkl.ads.model.Advertisement;
import ru.kinkl.ads.model.User;
import ru.kinkl.ads.persistence.AdvertisementDao;
import ru.kinkl.ads.persistence.UserDao;

import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = "/ads")
public class AdvertisementController {
    @Autowired
    private AdvertisementDao advertisementDao;

    @Autowired
    private UserDao userDao;

    @RequestMapping(method = RequestMethod.GET, headers = {"Accept=application/json"})
    public @ResponseBody AdvertisementDto[] getAll() {
        List<Advertisement> all = advertisementDao.findAll();
        if (all == null || all.size() == 0) {
            return null;
        }
        List<AdvertisementDto> dto = new ArrayList<AdvertisementDto>();
        for (Advertisement a : all) {
            dto.add(new AdvertisementDto(a.getId(), a.getText(), a.getUser().getName(), a.getDateTime()));
        }
        return dto.toArray(new AdvertisementDto[dto.size()]);
    }

    @RequestMapping(method = RequestMethod.POST, headers = {"Accept=application/json"})
    public void post(@RequestBody AdvertisementDto advertisement, Principal principal, HttpServletResponse response) {
        if (principal == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        Advertisement newAdvertisement = new Advertisement();
        newAdvertisement.setText(advertisement.getText());
        newAdvertisement.setDateTime(new Date());
        User user = userDao.findByNameIgnoreCase(principal.getName());
        newAdvertisement.setUser(user);
        advertisementDao.save(newAdvertisement);
        response.setStatus(HttpServletResponse.SC_CREATED);
    }

    @RequestMapping(method = RequestMethod.DELETE)
    public void delete(@RequestParam("id") Long id, Principal principal, HttpServletResponse response) {
        if (principal == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        boolean isAdmin = false;
        Collection<? extends GrantedAuthority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities();
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals("ROLE_ADMIN")) {
                isAdmin = true;
                break;
            }
        }
        if (!isAdmin) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        advertisementDao.delete(id);
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

}
