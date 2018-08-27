package com.ww.controller.rival;

import com.ww.service.SessionService;
import com.ww.service.rival.war.WarFastService;
//import com.ww.service.rival.war.WarFriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(value = "/war")
public class WarController {

    @Autowired
    SessionService sessionService;

    @Autowired
    WarFastService warFastService;

//    @Autowired
//    WarFriendService warFriendService;

    @RequestMapping(value = "/startFast", method = RequestMethod.POST)
    public Map startFast() {
        return warFastService.startFast();
    }
    @RequestMapping(value = "/cancelFast", method = RequestMethod.POST)
    public Map cancelFast() {
        return warFastService.cancelFast();
    }

//    @RequestMapping(value = "/startFriend", method = RequestMethod.POST)
//    public Map startFriend(@RequestBody Map<String, Object> payload) {
//        if (!payload.containsKey("tag")) {
//            throw new IllegalArgumentException();
//        }
//        return warFriendService.startFriend((String) payload.get("tag"));
//    }
//
//    @RequestMapping(value = "/cancelFriend", method = RequestMethod.POST)
//    public Map cancel() {
//        return warFriendService.cancelFriend();
//    }
//
//    @RequestMapping(value = "/acceptFriend", method = RequestMethod.POST)
//    public Map accept() {
//        return warFriendService.acceptFriend();
//    }
//
//    @RequestMapping(value = "/rejectFriend", method = RequestMethod.POST)
//    public Map reject() {
//        return warFriendService.rejectFriend();
//    }


}
