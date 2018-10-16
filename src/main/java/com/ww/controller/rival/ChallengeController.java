package com.ww.controller.rival;

import com.ww.model.constant.rival.challenge.ChallengeAccess;
import com.ww.model.constant.rival.challenge.ChallengeStatus;
import com.ww.model.constant.social.ResourceType;
import com.ww.model.dto.rival.challenge.ChallengeGlobalDTO;
import com.ww.model.dto.rival.challenge.ChallengePrivateDTO;
import com.ww.model.dto.rival.challenge.ChallengeSummaryDTO;
import com.ww.service.rival.challenge.ChallengeCreateService;
import com.ww.service.rival.challenge.ChallengeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/challenge")
public class ChallengeController {

    @Autowired
    private ChallengeService challengeService;
    @Autowired
    private ChallengeCreateService challengeCreateService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public Map create(@RequestBody Map<String, Object> payload) {
        if (!payload.containsKey("tags") || !payload.containsKey("access") || !payload.containsKey("resourceType") || !payload.containsKey("resourceCost") || !payload.containsKey("duration")) {
            throw new IllegalArgumentException();
        }
        List<String> tags = (List<String>) payload.get("tags");
        ChallengeAccess access = ChallengeAccess.valueOf((String) payload.get("access"));
        ResourceType resourceType = ResourceType.valueOf((String) payload.get("resourceType"));
        Long resourceCost = ((Integer) payload.get("resourceCost")).longValue();
        Integer duration = (Integer) payload.get("duration");
        return challengeCreateService.createPrivate(tags, access, resourceType, resourceCost, duration);
    }

    @RequestMapping(value = "/response", method = RequestMethod.POST)
    public Map response(@RequestBody Map<String, Object> payload) {
        if (!payload.containsKey("id")) {
            throw new IllegalArgumentException();
        }
        Long challengeId = ((Integer) payload.get("id")).longValue();
        return challengeService.response(challengeId);
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public List<ChallengePrivateDTO> list(@RequestBody Map<String, Object> payload) {
        if (!payload.containsKey("status") || !payload.containsKey("participant")) {
            throw new IllegalArgumentException();
        }
        ChallengeStatus status = ChallengeStatus.valueOf((String) payload.get("status"));
        Boolean participant = (Boolean) payload.get("participant");
        return challengeService.list(status, participant);
    }

    @RequestMapping(value = "/global", method = RequestMethod.POST)
    public ChallengeGlobalDTO global(@RequestBody Map<String, Object> payload) {
        return challengeService.global();
    }

    @RequestMapping(value = "/summary", method = RequestMethod.POST)
    public ChallengeSummaryDTO summary(@RequestBody Map<String, Object> payload) {
        if (!payload.containsKey("id")) {
            throw new IllegalArgumentException();
        }
        Long challengeId = ((Integer) payload.get("id")).longValue();
        return challengeService.summary(challengeId);
    }

    @RequestMapping(value = "/join", method = RequestMethod.POST)
    public Map<String, Object> join(@RequestBody Map<String, Object> payload) {
        if (!payload.containsKey("id")) {
            throw new IllegalArgumentException();
        }
        String creatorTag = payload.containsKey("creatorTag") ? (String) payload.get("creatorTag") : "";
        Long challengeId = ((Integer) payload.get("id")).longValue();
        return challengeService.join(challengeId, creatorTag);
    }
}
