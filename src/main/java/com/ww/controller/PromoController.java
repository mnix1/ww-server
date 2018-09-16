package com.ww.controller;

import com.ww.model.constant.wisie.MentalAttribute;
import com.ww.model.constant.wisie.WisdomAttribute;
import com.ww.model.entity.wisie.Wisie;
import com.ww.model.entity.wisie.ProfileWisie;
import com.ww.model.entity.social.Profile;
import com.ww.service.SessionService;
import com.ww.service.book.ProfileBookService;
import com.ww.service.wisie.WisieService;
import com.ww.service.wisie.ProfileWisieService;
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
    ProfileWisieService profileWisieService;

    @Autowired
    WisieService wisieService;

    @RequestMapping(value = "/promo", method = RequestMethod.GET)
    public Map promo() {
        Map<String, Object> model = new HashMap<>();
        Profile profile = profileService.getProfile();
        for (int i = 0; i < 1; i++) {
            Wisie wisie = wisieService.randomWisieForProfile(profile.getId());
            profileWisieService.addWisie(profile, wisie);
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
            Wisie wisie = wisieService.randomWisieForProfile(profile.getId());
            profileWisieService.addWisie(profile, wisie);
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
        List<ProfileWisie> team = profileWisieService.listTeam(profile.getId());
        for (ProfileWisie wisie : team) {
            for (MentalAttribute attribute : MentalAttribute.values()) {
                wisie.setMentalAttributeValue(attribute, Math.pow(wisie.getMentalAttributeValue(attribute), 1.1) + 100);
            }
        }
        profileWisieService.saveTeam(team);
        return model;
    }

    @RequestMapping(value = "/superTeamWisdom", method = RequestMethod.GET)
    public Map superTeamWisdom() {
        Map<String, Object> model = new HashMap<>();
        Profile profile = profileService.getProfile();
        List<ProfileWisie> team = profileWisieService.listTeam(profile.getId());
        for (ProfileWisie wisie : team) {
            for (WisdomAttribute attribute : WisdomAttribute.values()) {
                wisie.setWisdomAttributeValue(attribute, Math.pow(wisie.getWisdomAttributeValue(attribute), 1.1)+ 100);
            }
        }
        profileWisieService.saveTeam(team);
        return model;
    }


}
