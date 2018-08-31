package com.ww.controller;

import com.ww.model.constant.hero.MentalAttribute;
import com.ww.model.constant.hero.WisdomAttribute;
import com.ww.model.dto.hero.HeroDTO;
import com.ww.model.dto.hero.ProfileHeroDTO;
import com.ww.service.SessionService;
import com.ww.service.hero.HeroService;
import com.ww.service.hero.ProfileHeroService;
import com.ww.service.social.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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

    @RequestMapping(value = "/listProfileHero", method = RequestMethod.GET)
    public List<ProfileHeroDTO> listProfileHero() {
        return profileHeroService.list();
    }

    @RequestMapping(value = "/teamSave", method = RequestMethod.POST)
    public Map<String, Object> teamSave(@RequestBody Map<String, Object> payload) {
        if (!payload.containsKey("ids")) {
            throw new IllegalArgumentException();
        }
        List<Long> ids = ((List<Integer>) payload.get("ids")).stream().map(Integer::longValue).collect(Collectors.toList());
        return profileHeroService.teamSave(ids);
    }

    @RequestMapping(value = "/upgradeHero", method = RequestMethod.POST)
    public Map<String, Object> upgradeHero(@RequestBody Map<String, Object> payload) {
        if (!payload.containsKey("id") || !payload.containsKey("attribute")) {
            throw new IllegalArgumentException();
        }
        String attribute = (String) payload.get("attribute");
        Long profileHeroId = ((Integer) payload.get("id")).longValue();
        WisdomAttribute wisdomAttribute = WisdomAttribute.fromString(attribute);
        MentalAttribute mentalAttribute = MentalAttribute.fromString(attribute);
        return profileHeroService.upgradeHero(profileHeroId, wisdomAttribute, mentalAttribute);
    }

}
