package com.ww.controller;

import com.ww.model.dto.book.ProfileBookDTO;
import com.ww.model.dto.social.ProfileResourcesDTO;
import com.ww.model.entity.social.Profile;
import com.ww.service.SessionService;
import com.ww.service.book.ProfileBookService;
import com.ww.service.social.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/profile")
public class ProfileController {

    @Autowired
    ProfileService profileService;

    @Autowired
    ProfileBookService profileBookService;

    @Autowired
    SessionService sessionService;

    @RequestMapping(value = "/profileTag", method = RequestMethod.GET)
    public Map profileTag(Principal user) {
        Map<String, Object> model = new HashMap<>();
        String authId = profileService.getAuthId(user);
        if (authId != null) {
            Profile profile = profileService.createOrRetrieveProfile(authId);
            sessionService.setProfileId(profile.getId());
            model.put("profileTag", profile.getTag());
        }
        return model;
    }

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public ProfileResourcesDTO profile() {
        return new ProfileResourcesDTO(profileService.getProfile());
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
}
