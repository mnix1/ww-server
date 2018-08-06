package com.ww.controller.rival;

import com.ww.service.SessionService;
import com.ww.service.rival.battle.BattleFastService;
import com.ww.service.rival.battle.BattleFriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(value = "/battle")
public class BattleController {

    @Autowired
    SessionService sessionService;

    @Autowired
    BattleFastService battleFastService;

    @Autowired
    BattleFriendService battleFriendService;

    @RequestMapping(value = "/startFast", method = RequestMethod.POST)
    public Map startFast() {
        return battleFastService.startFast();
    }
    @RequestMapping(value = "/cancelFast", method = RequestMethod.POST)
    public Map cancelFast() {
        return battleFastService.cancelFast();
    }

    @RequestMapping(value = "/startFriend", method = RequestMethod.POST)
    public Map startFriend(@RequestBody Map<String, Object> payload) {
        if (!payload.containsKey("tag")) {
            throw new IllegalArgumentException();
        }
        return battleFriendService.startFriend((String) payload.get("tag"));
    }

    @RequestMapping(value = "/cancelFriend", method = RequestMethod.POST)
    public Map cancel() {
        return battleFriendService.cancelFriend();
    }

    @RequestMapping(value = "/acceptFriend", method = RequestMethod.POST)
    public Map accept() {
        return battleFriendService.acceptFriend();
    }

    @RequestMapping(value = "/rejectFriend", method = RequestMethod.POST)
    public Map reject() {
        return battleFriendService.rejectFriend();
    }


}
