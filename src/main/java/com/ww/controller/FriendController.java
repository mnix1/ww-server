package com.ww.controller;

import com.ww.model.dto.social.FriendDTO;
import com.ww.service.social.FriendService;
import com.ww.service.social.ProfileService;
import com.ww.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public Map add(@RequestBody Map<String, Object> payload) {
        return friendService.add((String) payload.get("tag"));
    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)

    public Map delete(@RequestBody Map<String, Object> payload) {
        return friendService.delete((String) payload.get("tag"));
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<FriendDTO> list() {
        return friendService.list();
    }

    @RequestMapping(value = "/suggest", method = RequestMethod.GET)
    public Map suggest() {
        return friendService.suggest();
    }
}
