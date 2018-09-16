package com.ww.controller.rival;

import com.ww.model.constant.rival.challenge.ChallengeStatus;
import com.ww.model.dto.rival.challenge.ChallengeInfoDTO;
import com.ww.model.dto.rival.challenge.ChallengeSummaryDTO;
import com.ww.service.SessionService;
import com.ww.service.rival.RivalChallengeService;
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
    SessionService sessionService;

    @Autowired
    RivalChallengeService rivalChallengeService;

    @RequestMapping(value = "/friendInit", method = RequestMethod.POST)
    public Map startFriend(@RequestBody Map<String, Object> payload) {
        if (!payload.containsKey("tags")) {
            throw new IllegalArgumentException();
        }
        List<String> tags = (List<String>) payload.get("tags");
        return rivalChallengeService.friendInit(tags);
    }

    @RequestMapping(value = "/response", method = RequestMethod.POST)
    public Map response(@RequestBody Map<String, Object> payload) {
        if (!payload.containsKey("id")) {
            throw new IllegalArgumentException();
        }
        Long challengeId = ((Integer) payload.get("id")).longValue();
        return rivalChallengeService.response(challengeId);
    }

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public List<ChallengeInfoDTO> list(@RequestBody Map<String, Object> payload) {
        ChallengeStatus status = null;
        if (payload.containsKey("status")) {
            try {
                status = ChallengeStatus.valueOf((String) payload.get("status"));
            } catch (IllegalArgumentException e) {
            }
        }
        return rivalChallengeService.list(status);
    }

    @RequestMapping(value = "/summary", method = RequestMethod.POST)
    public ChallengeSummaryDTO summary(@RequestBody Map<String, Object> payload) {
        if (!payload.containsKey("challengeId")) {
            throw new IllegalArgumentException();
        }
        Long challengeId = ((Integer) payload.get("challengeId")).longValue();
        return rivalChallengeService.summary(challengeId);
    }
}
