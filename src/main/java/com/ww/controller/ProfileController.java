package com.ww.controller;

import com.ww.model.entity.social.Profile;
import com.ww.service.ProfileService;
import com.ww.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ProfileController {

    @Autowired
    ProfileService profileService;

    @Autowired
    SessionService sessionService;

    @RequestMapping(value="/profile", method = RequestMethod.GET)
    public Map profileId(Principal user){
        Map<String, Long> model = new HashMap<>();
        String authId = profileService.getAuthId(user);
        if (authId != null) {
            Profile profile = profileService.createOrRetrieveProfile(authId);
            sessionService.setProfileId(profile.getId());
            model.put("profileId", profile.getId());
        }
        return model;
    }
}
