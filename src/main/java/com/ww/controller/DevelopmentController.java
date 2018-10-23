package com.ww.controller;

import com.ww.model.entity.outside.social.Profile;
import com.ww.repository.outside.wisie.ProfileWisieRepository;
import com.ww.service.social.FriendService;
import com.ww.service.social.ProfileService;
import com.ww.service.wisie.ProfileWisieService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/dev")
@AllArgsConstructor
public class DevelopmentController {

    private final ProfileService profileService;
    private final FriendService friendService;
    private final ProfileWisieRepository profileWisieRepository;

    @RequestMapping(value = "/initFriends", method = RequestMethod.GET)
    public Map initFriends() {
        Map<String, Object> model = new HashMap<>();
        Profile profile = profileService.getProfile();
        friendService.add(profileService.getProfile(1L).getTag());
        friendService.add(profileService.getProfile(2L).getTag());
        friendService.add(profileService.getProfile(3L).getTag());
        friendService.add(profileService.getProfile(4L).getTag());
        friendService.add(1L, profile.getTag());
        friendService.add(2L, profile.getTag());
        friendService.add(3L, profile.getTag());
        friendService.add(4L, profile.getTag());
        return model;
    }

    @RequestMapping(value = "/zeroStepIndex", method = RequestMethod.GET)
    public Map zeroStepIndex() {
        Profile profile = profileService.getProfile();
        profile.setIntroductionStepIndex(0);
        profileService.save(profile);
        return null;
    }

    @RequestMapping(value = "/cleanProfile", method = RequestMethod.GET)
    public Map cleanProfile() {
        Profile profile = profileService.getProfile();
        Profile newProfile = new Profile(profile.getAuthId(), profile.getName(), profile.getLanguage());
        newProfile.setId(profile.getId());
        profileWisieRepository.deleteAll(profile.getWisies());
        profileService.save(newProfile);
        return null;
    }
}
