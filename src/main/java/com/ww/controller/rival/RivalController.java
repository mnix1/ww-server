package com.ww.controller.rival;

import com.ww.model.constant.rival.RivalType;
import com.ww.service.SessionService;
import com.ww.service.rival.RivalFriendService;
import com.ww.service.rival.battle.BattleFastService;
import com.ww.service.rival.war.WarFastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(value = "/rival")
public class RivalController {

    @Autowired
    SessionService sessionService;

    @Autowired
    RivalFriendService rivalFriendService;

    @Autowired
    BattleFastService battleFastService;

    @Autowired
    WarFastService warFastService;

    @RequestMapping(value = "/startFast", method = RequestMethod.POST)
    public Map startFast(@RequestBody Map<String, Object> payload) {
        if (!payload.containsKey("type")) {
            throw new IllegalArgumentException();
        }
        RivalType type = RivalType.valueOf((String) payload.get("type"));
        if (type == RivalType.BATTLE) {
            return battleFastService.startFast();
        } else if (type == RivalType.WAR) {
            return warFastService.startFast();
        }
        throw new IllegalArgumentException();
    }

    @RequestMapping(value = "/cancelFast", method = RequestMethod.POST)
    public Map cancelFast(@RequestBody Map<String, Object> payload) {
        if (!payload.containsKey("type")) {
            throw new IllegalArgumentException();
        }
        RivalType type = RivalType.valueOf((String) payload.get("type"));
        if (type == RivalType.BATTLE) {
            return battleFastService.cancelFast();
        } else if (type == RivalType.WAR) {
            return warFastService.cancelFast();
        }
        throw new IllegalArgumentException();
    }

    @RequestMapping(value = "/startFriend", method = RequestMethod.POST)
    public Map startFriend(@RequestBody Map<String, Object> payload) {
        if (!payload.containsKey("tag")) {
            throw new IllegalArgumentException();
        }
        if (!payload.containsKey("type")) {
            throw new IllegalArgumentException();
        }
        RivalType type = RivalType.valueOf((String) payload.get("type"));
        return rivalFriendService.startFriend((String) payload.get("tag"), type);
    }

    @RequestMapping(value = "/cancelFriend", method = RequestMethod.POST)
    public Map cancel() {
        return rivalFriendService.cancelFriend();
    }

    @RequestMapping(value = "/acceptFriend", method = RequestMethod.POST)
    public Map accept() {
        return rivalFriendService.acceptFriend();
    }

    @RequestMapping(value = "/rejectFriend", method = RequestMethod.POST)
    public Map reject() {
        return rivalFriendService.rejectFriend();
    }
}
