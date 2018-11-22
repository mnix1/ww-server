package com.ww.controller;

import com.ww.model.constant.wisie.MentalAttribute;
import com.ww.model.constant.wisie.WisdomAttribute;
import com.ww.model.dto.wisie.ProfileWisieDTO;
import com.ww.service.social.ProfileService;
import com.ww.service.wisie.ProfileWisieEvolutionService;
import com.ww.service.wisie.ProfileWisieService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/wisie")
@AllArgsConstructor
public class WisieController {

    private final ProfileService profileService;
    private final ProfileWisieService profileWisieService;
    private final ProfileWisieEvolutionService profileWisieEvolutionService;

    @RequestMapping(value = "/experiment", method = RequestMethod.GET)
    public Map<String, Object> experiment() {
        return profileWisieService.experiment(null);
    }

    @RequestMapping(value = "/listProfileWisie", method = RequestMethod.GET)
    public List<ProfileWisieDTO> listProfileWisie() {
        return profileWisieService.list();
    }

    @RequestMapping(value = "/teamSave", method = RequestMethod.POST)
    public Map<String, Object> teamSave(@RequestBody Map<String, Object> payload) {
        if (!payload.containsKey("ids")) {
            throw new IllegalArgumentException();
        }
        List<Long> ids = ((List<Integer>) payload.get("ids")).stream().map(Integer::longValue).collect(Collectors.toList());
        return profileWisieService.teamSave(ids);
    }

    @RequestMapping(value = "/upgradeAttribute", method = RequestMethod.POST)
    public Map<String, Object> upgradeAttribute(@RequestBody Map<String, Object> payload) {
        if (!payload.containsKey("id") || !payload.containsKey("attribute")) {
            throw new IllegalArgumentException();
        }
        String attribute = (String) payload.get("attribute");
        Long profileWisieId = ((Integer) payload.get("id")).longValue();
        WisdomAttribute wisdomAttribute = WisdomAttribute.fromString(attribute);
        MentalAttribute mentalAttribute = MentalAttribute.fromString(attribute);
        return profileWisieEvolutionService.upgradeAttribute(profileWisieId, profileService.getProfileId(), wisdomAttribute, mentalAttribute);
    }

    @RequestMapping(value = "/changeHobby", method = RequestMethod.POST)
    public Map<String, Object> changeHobby(@RequestBody Map<String, Object> payload) {
        if (!payload.containsKey("id") || !payload.containsKey("hobby")) {
            throw new IllegalArgumentException();
        }
        String hobby = (String) payload.get("hobby");
        Long profileWisieId = ((Integer) payload.get("id")).longValue();
        return profileWisieEvolutionService.changeHobby(profileWisieId, hobby, profileService.getProfileId());
    }

    @RequestMapping(value = "/changeSkill", method = RequestMethod.POST)
    public Map<String, Object> changeSkill(@RequestBody Map<String, Object> payload) {
        if (!payload.containsKey("id") || !payload.containsKey("skill")) {
            throw new IllegalArgumentException();
        }
        String skill = (String) payload.get("skill");
        Long profileWisieId = ((Integer) payload.get("id")).longValue();
        return profileWisieEvolutionService.changeSkill(profileWisieId, skill);
    }

}
