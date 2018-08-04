package com.ww.controller.rival;

import com.ww.model.constant.rival.challenge.ChallengeStatus;
import com.ww.model.dto.rival.challenge.ChallengeInfoDTO;
import com.ww.model.dto.rival.challenge.ChallengeSummaryDTO;
import com.ww.model.dto.rival.challenge.ChallengeTaskDTO;
import com.ww.service.SessionService;
import com.ww.service.rival.ChallengeService;
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
    ChallengeService challengeService;

    @RequestMapping(value = "/startFriend", method = RequestMethod.POST)
    public ChallengeTaskDTO startFriend(@RequestBody Map<String, Object> payload) {
        if (!payload.containsKey("tags")) {
            throw new IllegalArgumentException();
        }
        List<String> tags = (List<String>) payload.get("tags");
        return challengeService.startFriend(tags);
    }

    @RequestMapping(value = "/startFast", method = RequestMethod.POST)
    public ChallengeTaskDTO startFast(@RequestBody Map<String, Object> payload) {
        return challengeService.startFast();
    }

    @RequestMapping(value = "/startResponse", method = RequestMethod.POST)
    public ChallengeTaskDTO startResponse(@RequestBody Map<String, Object> payload) {
        if (!payload.containsKey("challengeId")) {
            throw new IllegalArgumentException();
        }
        Long challengeId = ((Integer) payload.get("challengeId")).longValue();
        return challengeService.startResponse(challengeId);
    }

    @RequestMapping(value = "/nextTask", method = RequestMethod.POST)
    public ChallengeTaskDTO nextTask(@RequestBody Map<String, Object> payload) {
        if (!payload.containsKey("challengeId")) {
            throw new IllegalArgumentException();
        }
        Long challengeId = ((Integer) payload.get("challengeId")).longValue();
        return challengeService.nextTask(challengeId);
    }

    @RequestMapping(value = "/endTask", method = RequestMethod.POST)
    public Map endTask(@RequestBody Map<String, Object> payload) {
        if (!payload.containsKey("challengeId") || !payload.containsKey("answerId")) {
            throw new IllegalArgumentException();
        }
        Long challengeId = ((Integer) payload.get("challengeId")).longValue();
        Long answerId = ((Integer) payload.get("answerId")).longValue();
        return challengeService.endTask(challengeId, answerId);
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
        return challengeService.list(status);
    }

    @RequestMapping(value = "/summary", method = RequestMethod.POST)
    public ChallengeSummaryDTO summary(@RequestBody Map<String, Object> payload) {
        if (!payload.containsKey("challengeId")) {
            throw new IllegalArgumentException();
        }
        Long challengeId = ((Integer) payload.get("challengeId")).longValue();
        return challengeService.summary(challengeId);
    }
}
