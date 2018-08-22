package com.ww.controller;

import com.ww.model.dto.hero.HeroDTO;
import com.ww.service.SessionService;
import com.ww.service.hero.HeroService;
import com.ww.service.hero.ProfileHeroService;
import com.ww.service.social.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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

    @Autowired
    ProfileHeroService profileHeroService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<HeroDTO> list() {
        return heroService.list();
    }

    @RequestMapping(value = "/experiment", method = RequestMethod.GET)
    public Map<String, Object> experiment() {
        return heroService.experiment();
    }

}
