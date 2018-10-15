package com.ww.controller;

import com.ww.model.constant.wisie.WisorType;
import com.ww.model.dto.book.ProfileBookDTO;
import com.ww.model.dto.social.ExtendedProfileResourcesDTO;
import com.ww.service.book.ProfileBookService;
import com.ww.service.social.AuthProfileService;
import com.ww.service.social.ProfileService;
import com.ww.service.wisie.WisieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/profile")
public class ProfileController {

    @Autowired
    AuthProfileService authProfileService;

    @Autowired
    ProfileService profileService;

    @Autowired
    ProfileBookService profileBookService;

    @Autowired
    WisieService wisieService;

    @RequestMapping(value = "/changeWisor", method = RequestMethod.POST)
    public Map changeWisor(@RequestBody Map<String, Object> payload) {
        if (!payload.containsKey("wisor")) {
            throw new IllegalArgumentException();
        }
        WisorType type = WisorType.valueOf((String) payload.get("wisor"));
        return profileService.changeWisor(type);
    }

    @RequestMapping(value = "/changeName", method = RequestMethod.POST)
    public Map changeName(@RequestBody Map<String, Object> payload) {
        if (!payload.containsKey("name")) {
            throw new IllegalArgumentException();
        }
        String name = (String) payload.get("name");
        return profileService.changeName(name);
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public ExtendedProfileResourcesDTO profile(Principal user) {
        return authProfileService.authProfile(user);
    }

    @RequestMapping(value = "/testSignIn", method = RequestMethod.GET)
    public Boolean testSignIn() {
        return true;
    }

    @RequestMapping(value = "/listBook", method = RequestMethod.GET)
    public List<ProfileBookDTO> listBook() {
        return profileBookService.listBook();
    }

    @RequestMapping(value = "/startReadBook", method = RequestMethod.POST)
    public Map startReadBook(@RequestBody Map<String, Object> payload) {
        if (!payload.containsKey("id")) {
            throw new IllegalArgumentException();
        }
        Long profileBookId = ((Integer) payload.get("id")).longValue();
        return profileBookService.startReadBook(profileBookId);
    }

    @RequestMapping(value = "/stopReadBook", method = RequestMethod.POST)
    public Map stopReadBook(@RequestBody Map<String, Object> payload) {
        if (!payload.containsKey("id")) {
            throw new IllegalArgumentException();
        }
        Long profileBookId = ((Integer) payload.get("id")).longValue();
        return profileBookService.stopReadBook(profileBookId);
    }

    @RequestMapping(value = "/discardBook", method = RequestMethod.POST)
    public Map discardBook(@RequestBody Map<String, Object> payload) {
        if (!payload.containsKey("id")) {
            throw new IllegalArgumentException();
        }
        Long profileBookId = ((Integer) payload.get("id")).longValue();
        return profileBookService.discardBook(profileBookId);
    }

    @RequestMapping(value = "/claimRewardBook", method = RequestMethod.POST)
    public Map claimRewardBook(@RequestBody Map<String, Object> payload) {
        if (!payload.containsKey("id")) {
            throw new IllegalArgumentException();
        }
        Long profileBookId = ((Integer) payload.get("id")).longValue();
        return profileBookService.claimRewardBook(profileBookId);
    }

    @RequestMapping(value = "/speedUpBook", method = RequestMethod.POST)
    public Map speedUpBook(@RequestBody Map<String, Object> payload) {
        if (!payload.containsKey("id")) {
            throw new IllegalArgumentException();
        }
        Long profileBookId = ((Integer) payload.get("id")).longValue();
        return profileBookService.speedUpBook(profileBookId);
    }

}
