package com.ww.controller;

import com.ww.model.entity.social.Profile;
import com.ww.service.SessionService;
import com.ww.service.rival.BattleService;
import com.ww.service.social.FriendService;
import com.ww.service.social.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/dev")
public class DevelopmentController {

    @Autowired
    SessionService sessionService;

    @Autowired
    ProfileService profileService;

    @Autowired
    FriendService friendService;

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
}
