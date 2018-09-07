package com.ww.controller;

import com.ww.model.constant.wisie.MentalAttribute;
import com.ww.model.constant.wisie.WisdomAttribute;
import com.ww.model.dto.wisie.WisieDTO;
import com.ww.model.dto.wisie.ProfileWisieDTO;
import com.ww.service.SessionService;
import com.ww.service.wisie.WisieService;
import com.ww.service.wisie.ProfileWisieService;
import com.ww.service.social.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/wisie")
public class WisieController {

    @Autowired
    ProfileService profileService;

    @Autowired
    SessionService sessionService;

    @Autowired
    WisieService wisieService;

    @Autowired
    ProfileWisieService profileWisieService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<WisieDTO> list() {
        return wisieService.list();
    }

    @RequestMapping(value = "/experiment", method = RequestMethod.GET)
    public Map<String, Object> experiment() {
        return wisieService.experiment();
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

    @RequestMapping(value = "/upgradeWisie", method = RequestMethod.POST)
    public Map<String, Object> upgradeWisie(@RequestBody Map<String, Object> payload) {
        if (!payload.containsKey("id") || !payload.containsKey("attribute")) {
            throw new IllegalArgumentException();
        }
        String attribute = (String) payload.get("attribute");
        Long profileWisieId = ((Integer) payload.get("id")).longValue();
        WisdomAttribute wisdomAttribute = WisdomAttribute.fromString(attribute);
        MentalAttribute mentalAttribute = MentalAttribute.fromString(attribute);
        return profileWisieService.upgradeWisie(profileWisieId, wisdomAttribute, mentalAttribute);
    }

}
