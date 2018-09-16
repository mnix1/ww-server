package com.ww.controller.rival;

import com.ww.model.constant.rival.RivalImportance;
import com.ww.model.constant.rival.RivalType;
import com.ww.model.dto.social.ClassificationProfileDTO;
import com.ww.service.SessionService;
import com.ww.service.rival.RivalFriendService;
import com.ww.service.rival.battle.BattleRandomOpponentService;
import com.ww.service.rival.battle.BattleService;
import com.ww.service.rival.war.WarRandomOpponentService;
import com.ww.service.rival.war.WarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/rival")
public class RivalController {

    @Autowired
    SessionService sessionService;

    @Autowired
    RivalFriendService rivalFriendService;

    @Autowired
    BattleRandomOpponentService battleRandomOpponentService;

    @Autowired
    BattleService battleService;

    @Autowired
    WarService warService;

    @Autowired
    WarRandomOpponentService warRandomOpponentService;

    @RequestMapping(value = "/classification", method = RequestMethod.POST)
    public List<ClassificationProfileDTO> classification(@RequestBody Map<String, Object> payload) {
        if (!payload.containsKey("type")) {
            throw new IllegalArgumentException();
        }
        RivalType type = RivalType.valueOf((String) payload.get("type"));
        if (type == RivalType.BATTLE) {
            return battleService.classification(type);
        } else if (type == RivalType.WAR) {
            return warService.classification(type);
        }
        throw new IllegalArgumentException();
    }

    @RequestMapping(value = "/startRandomOpponent", method = RequestMethod.POST)
    public Map startRandomOpponent(@RequestBody Map<String, Object> payload) {
        if (!payload.containsKey("type") || !payload.containsKey("importance")) {
            throw new IllegalArgumentException();
        }
        RivalType type = RivalType.valueOf((String) payload.get("type"));
        RivalImportance importance = RivalImportance.valueOf((String) payload.get("importance"));
        if (type == RivalType.BATTLE) {
            return battleRandomOpponentService.start(importance);
        } else if (type == RivalType.WAR) {
            return warRandomOpponentService.start(importance);
        }
        throw new IllegalArgumentException();
    }

    @RequestMapping(value = "/cancelRandomOpponent", method = RequestMethod.POST)
    public Map cancelRandomOpponent(@RequestBody Map<String, Object> payload) {
        if (!payload.containsKey("type") || !payload.containsKey("importance")) {
            throw new IllegalArgumentException();
        }
        RivalType type = RivalType.valueOf((String) payload.get("type"));
        RivalImportance importance = RivalImportance.valueOf((String) payload.get("importance"));
        if (type == RivalType.BATTLE) {
            return battleRandomOpponentService.cancel(importance);
        } else if (type == RivalType.WAR) {
            return warRandomOpponentService.cancel(importance);
        }
        throw new IllegalArgumentException();
    }

    @RequestMapping(value = "/startFriend", method = RequestMethod.POST)
    public Map startFriend(@RequestBody Map<String, Object> payload) {
        if (!payload.containsKey("tag") || !payload.containsKey("type")) {
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
