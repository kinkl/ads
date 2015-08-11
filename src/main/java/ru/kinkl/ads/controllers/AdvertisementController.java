package ru.kinkl.ads.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import ru.kinkl.ads.dto.AdvertisementDto;
import ru.kinkl.ads.dto.Ext;
import ru.kinkl.ads.dto.VoteRegistrationResultDto;
import ru.kinkl.ads.model.Advertisement;
import ru.kinkl.ads.model.User;
import ru.kinkl.ads.model.Vote;
import ru.kinkl.ads.persistence.AdvertisementDao;
import ru.kinkl.ads.persistence.UserDao;
import ru.kinkl.ads.persistence.VoteDao;

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

    @Autowired
    private VoteDao voteDao;

    @RequestMapping(method = RequestMethod.GET, headers = {"Accept=application/json"})
    public @ResponseBody AdvertisementDto[] getAll() {
        List<Advertisement> all = advertisementDao.findAll();
        if (all == null || all.size() == 0) {
            return null;
        }
        List<AdvertisementDto> dto = new ArrayList<AdvertisementDto>();
        for (Advertisement a : all) {
            Integer advertisementRate = voteDao.getAdvertisementRate(a);
            dto.add(new AdvertisementDto(a.getId(), a.getText(), a.getUser().getName(), a.getDateTime(), advertisementRate));
        }
        return dto.toArray(new AdvertisementDto[dto.size()]);
    }

    @RequestMapping(value = "/top-rated", method = RequestMethod.GET, headers = {"Accept=application/json"})
    public @ResponseBody Ext<List<AdvertisementDto>> getTopRated() {
        List<Object[]> topRated = advertisementDao.getTopRated();
        if (topRated == null) {
            return new Ext<List<AdvertisementDto>>(null, false, "Error!");
        }
        List<AdvertisementDto> dto = new ArrayList<AdvertisementDto>();
        for (Object[] objects : topRated) {
            dto.add(new AdvertisementDto(Long.parseLong(objects[0].toString()), (String)objects[1], (String)objects[2], (Date)objects[3], (Integer)objects[4]));
        }
        return new Ext<List<AdvertisementDto>>(dto);
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

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.POST, value = "/upvote")
    public @ResponseBody Ext<VoteRegistrationResultDto> upvote(@RequestBody AdvertisementDto advertisement, Principal principal) {
        User user = userDao.findByNameIgnoreCase(principal.getName());
        Advertisement foundedAdvertisement = advertisementDao.findById(advertisement.getId());
        if (user == null || foundedAdvertisement == null) {
            return new Ext<VoteRegistrationResultDto>(new VoteRegistrationResultDto(false), false, "User or advertisement not found");
        }
        Vote vote = voteDao.findByAdvertisementAndUser(foundedAdvertisement, user);
        if (vote == null) {
            vote = new Vote();
            vote.setAdvertisement(foundedAdvertisement);
            vote.setUser(user);
            vote.setValue(1);
            voteDao.save(vote);
            return new Ext<VoteRegistrationResultDto>(new VoteRegistrationResultDto(true));
        }
        return new Ext<VoteRegistrationResultDto>(new VoteRegistrationResultDto(false, "Current vote is already registered"), true, "Current vote is already registered");
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(method = RequestMethod.POST, value = "/downvote")
    public @ResponseBody Ext<VoteRegistrationResultDto> downvote(@RequestBody AdvertisementDto advertisement, Principal principal) {
        User user = userDao.findByNameIgnoreCase(principal.getName());
        Advertisement foundedAdvertisement = advertisementDao.findById(advertisement.getId());
        if (user == null || foundedAdvertisement == null) {
            return new Ext<VoteRegistrationResultDto>(new VoteRegistrationResultDto(false), false, "User or advertisement not found");
        }
        Vote vote = voteDao.findByAdvertisementAndUser(foundedAdvertisement, user);
        if (vote == null) {
            vote = new Vote();
            vote.setAdvertisement(foundedAdvertisement);
            vote.setUser(user);
            vote.setValue(-1);
            voteDao.save(vote);
            return new Ext<VoteRegistrationResultDto>(new VoteRegistrationResultDto(true));
        }
        return new Ext<VoteRegistrationResultDto>(new VoteRegistrationResultDto(false, "Current vote is already registered"), true, "Current vote is already registered");
    }
}
