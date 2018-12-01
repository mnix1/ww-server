package com.ww.controller.rival;

import com.ww.model.constant.rival.RivalImportance;
import com.ww.model.constant.rival.RivalType;
import com.ww.model.dto.social.ClassificationDTO;
import com.ww.service.rival.global.RivalClassificationService;
import com.ww.service.rival.init.RivalInitFriendService;
import com.ww.service.rival.init.RivalInitRandomOpponentService;
import com.ww.service.rival.init.RivalInitTrainingService;
import com.ww.service.social.ProfileService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static com.ww.helper.ModelHelper.putErrorCode;

@RestController
@RequestMapping(value = "/rival")
@AllArgsConstructor
public class RivalController {

    private final RivalInitFriendService rivalInitFriendService;
    private final RivalInitRandomOpponentService rivalInitRandomOpponentService;
    private final RivalInitTrainingService rivalInitTrainingService;
    private final RivalClassificationService rivalClassificationService;
    private final ProfileService profileService;

    @RequestMapping(value = "/classification", method = RequestMethod.POST)
    public ClassificationDTO classification(@RequestBody Map<String, Object> payload) {
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
        try {
            RivalType type = RivalType.valueOf((String) payload.get("type"));
            RivalImportance importance = RivalImportance.valueOf((String) payload.get("importance"));
            if (importance.isTraining()) {
                return rivalInitTrainingService.start(type, profileService.getProfileId());
            }
            return rivalInitRandomOpponentService.start(type, importance, profileService.getProfileId());
        } catch (Exception e) {
            return putErrorCode(new HashMap<>());
        }
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
