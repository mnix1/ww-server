package com.ww.controller;

import com.ww.model.constant.hero.MentalAttribute;
import com.ww.model.constant.hero.WisdomAttribute;
import com.ww.model.entity.hero.Hero;
import com.ww.model.entity.hero.ProfileHero;
import com.ww.model.entity.social.Profile;
import com.ww.service.SessionService;
import com.ww.service.book.ProfileBookService;
import com.ww.service.hero.HeroService;
import com.ww.service.hero.ProfileHeroService;
import com.ww.service.social.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/promo")
public class PromoController {

    @Autowired
    ProfileService profileService;

    @Autowired
    ProfileBookService profileBookService;

    @Autowired
    ProfileHeroService profileHeroService;

    @Autowired
    HeroService heroService;

    @Autowired
    SessionService sessionService;

    @RequestMapping(value = "/promo", method = RequestMethod.GET)
    public Map promo() {
        Map<String, Object> model = new HashMap<>();
        Profile profile = profileService.getProfile();
        for (int i = 0; i < 1; i++) {
            Hero hero = heroService.randomHeroForProfile(profile.getId());
            profileHeroService.addHero(profile, hero);
        }
        profile.changeResources(100L, 100L, 100L, 100L);
        profileService.save(profile);
        return model;
    }

    @RequestMapping(value = "/superPromo", method = RequestMethod.GET)
    public Map superPromo() {
        Map<String, Object> model = new HashMap<>();
        Profile profile = profileService.getProfile();
        for (int i = 0; i < 4; i++) {
            Hero hero = heroService.randomHeroForProfile(profile.getId());
            profileHeroService.addHero(profile, hero);
        }
        profile.changeResources(1000L, 1000L, 1000L, 1000L);
        profileService.save(profile);
        return model;
    }

    @RequestMapping(value = "/superTeam", method = RequestMethod.GET)
    public Map superTeam() {
        Map<String, Object> model = new HashMap<>();
        superTeamMental();
        superTeamWisdom();
        return model;
    }

    @RequestMapping(value = "/superTeamMental", method = RequestMethod.GET)
    public Map superTeamMental() {
        Map<String, Object> model = new HashMap<>();
        Profile profile = profileService.getProfile();
        List<ProfileHero> team = profileHeroService.listTeam(profile.getId());
        for (ProfileHero hero : team) {
            for (MentalAttribute attribute : MentalAttribute.values()) {
                hero.setMentalAttributeValue(attribute, Math.pow(hero.getMentalAttributeValue(attribute), 1.1) + 100);
            }
        }
        profileHeroService.saveTeam(team);
        return model;
    }

    @RequestMapping(value = "/superTeamWisdom", method = RequestMethod.GET)
    public Map superTeamWisdom() {
        Map<String, Object> model = new HashMap<>();
        Profile profile = profileService.getProfile();
        List<ProfileHero> team = profileHeroService.listTeam(profile.getId());
        for (ProfileHero hero : team) {
            for (WisdomAttribute attribute : WisdomAttribute.values()) {
                hero.setWisdomAttributeValue(attribute, Math.pow(hero.getWisdomAttributeValue(attribute), 1.1)+ 100);
            }
        }
        profileHeroService.saveTeam(team);
        return model;
    }


}
