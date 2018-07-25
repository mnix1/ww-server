package com.ww.controller;

import com.ww.service.social.FriendService;
import com.ww.service.social.ProfileService;
import com.ww.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/friend")
public class FriendController {

    @Autowired
    ProfileService profileService;

    @Autowired
    SessionService sessionService;

    @Autowired
    FriendService friendService;

    @RequestMapping(value = "/request", method = RequestMethod.GET)
    public Map request(String tag) {
        return friendService.request(tag);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public Map list() {
        return friendService.list();
    }
}
