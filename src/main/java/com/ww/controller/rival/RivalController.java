package com.ww.controller.rival;

import com.ww.model.constant.rival.RivalImportance;
import com.ww.model.constant.rival.RivalType;
import com.ww.model.dto.social.ClassificationProfileSeasonDTO;
import com.ww.service.rival.global.RivalClassificationService;
import com.ww.service.rival.init.RivalInitFriendService;
import com.ww.service.rival.init.RivalInitRandomOpponentService;
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
    private RivalInitFriendService rivalInitFriendService;

    @Autowired
    private RivalInitRandomOpponentService rivalInitRandomOpponentService;

    @Autowired
    private RivalClassificationService rivalClassificationService;

    @RequestMapping(value = "/classification", method = RequestMethod.POST)
    public List<ClassificationProfileSeasonDTO> classification(@RequestBody Map<String, Object> payload) {
        if (!payload.containsKey("type")) {
            throw new IllegalArgumentException();
        }
        RivalType type = RivalType.valueOf((String) payload.get("type"));
        return rivalClassificationService.classification(type);
    }

    @RequestMapping(value = "/startRandomOpponent", method = RequestMethod.POST)
    public Map startRandomOpponent(@RequestBody Map<String, Object> payload) {
        if (!payload.containsKey("type") || !payload.containsKey("importance")) {
            throw new IllegalArgumentException();
        }
        RivalType type = RivalType.valueOf((String) payload.get("type"));
        RivalImportance importance = RivalImportance.valueOf((String) payload.get("importance"));
        return rivalInitRandomOpponentService.start(type, importance);
    }

    @RequestMapping(value = "/cancelRandomOpponent", method = RequestMethod.POST)
    public Map cancelRandomOpponent() {
        return rivalInitRandomOpponentService.cancel();
    }

    @RequestMapping(value = "/startFriend", method = RequestMethod.POST)
    public Map startFriend(@RequestBody Map<String, Object> payload) {
        if (!payload.containsKey("tag") || !payload.containsKey("type")) {
            throw new IllegalArgumentException();
        }
        RivalType type = RivalType.valueOf((String) payload.get("type"));
        return rivalInitFriendService.startFriend((String) payload.get("tag"), type);
    }

    @RequestMapping(value = "/cancelFriend", method = RequestMethod.POST)
    public Map cancel() {
        return rivalInitFriendService.cancelFriend();
    }

    @RequestMapping(value = "/acceptFriend", method = RequestMethod.POST)
    public Map accept() {
        return rivalInitFriendService.acceptFriend();
    }

    @RequestMapping(value = "/rejectFriend", method = RequestMethod.POST)
    public Map reject() {
        return rivalInitFriendService.rejectFriend();
    }
}
