package com.ww.controller;

import com.ww.model.constant.wisie.MentalAttribute;
import com.ww.model.constant.wisie.WisdomAttribute;
import com.ww.model.constant.wisie.WisieType;
import com.ww.model.container.Resources;
import com.ww.model.entity.outside.wisie.ProfileWisie;
import com.ww.model.entity.outside.social.Profile;
import com.ww.service.wisie.ProfileWisieService;
import com.ww.service.social.ProfileService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/_promo")
@AllArgsConstructor
public class PromoController {

    private final ProfileService profileService;
    private final ProfileWisieService profileWisieService;

    @RequestMapping(value = "/promo", method = RequestMethod.GET)
    public Map promo() {
        Map<String, Object> model = new HashMap<>();
        Profile profile = profileService.getProfile();
        for (int i = 0; i < 1; i++) {
            WisieType type = profileWisieService.randomWisieForProfile(profile.getId());
            profileWisieService.addWisie(profile, type);
        }
        profile.addResources(new Resources(100L, 100L, 100L, 100L));
        profileService.save(profile);
        return model;
    }

    @RequestMapping(value = "/superPromo", method = RequestMethod.GET)
    public Map superPromo() {
        Map<String, Object> model = new HashMap<>();
        Profile profile = profileService.getProfile();
        for (int i = 0; i < 4; i++) {
            WisieType type = profileWisieService.randomWisieForProfile(profile.getId());
            profileWisieService.addWisie(profile, type);
        }
        profile.addResources(new Resources(1000L, 1000L, 1000L, 1000L));
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
        List<ProfileWisie> team = profileWisieService.findAllInTeam(profile.getId());
        for (ProfileWisie wisie : team) {
            for (MentalAttribute attribute : MentalAttribute.values()) {
                wisie.setMentalAttributeValue(attribute, Math.pow(wisie.getMentalAttributeValue(attribute), 1.1) + 100);
            }
        }
        profileWisieService.save(team);
        return model;
    }

    @RequestMapping(value = "/superTeamWisdom", method = RequestMethod.GET)
    public Map superTeamWisdom() {
        Map<String, Object> model = new HashMap<>();
        Profile profile = profileService.getProfile();
        List<ProfileWisie> team = profileWisieService.findAllInTeam(profile.getId());
        for (ProfileWisie wisie : team) {
            for (WisdomAttribute attribute : WisdomAttribute.values()) {
                wisie.setWisdomAttributeValue(attribute, Math.pow(wisie.getWisdomAttributeValue(attribute), 1.1)+ 100);
            }
        }
        profileWisieService.save(team);
        return model;
    }


}
