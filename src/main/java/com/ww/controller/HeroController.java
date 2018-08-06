package com.ww.controller;

import com.ww.model.dto.hero.HeroDTO;
import com.ww.model.dto.social.FriendDTO;
import com.ww.model.dto.social.ProfileDTO;
import com.ww.model.entity.social.Profile;
import com.ww.service.SessionService;
import com.ww.service.hero.HeroService;
import com.ww.service.social.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/hero")
public class HeroController {

    @Autowired
    ProfileService profileService;

    @Autowired
    SessionService sessionService;

    @Autowired
    HeroService heroService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<HeroDTO> list() {
        return heroService.list();
    }

}
