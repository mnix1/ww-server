package com.ww.controller.rival;

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

    @RequestMapping(value = "/startResponse", method = RequestMethod.POST)
    public ChallengeTaskDTO startResponse(@RequestBody Map<String, Object> payload) {
        if (!payload.containsKey("challengeId")) {
            throw new IllegalArgumentException();
        }
        Long challengeId = ((Integer) payload.get("challengeId")).longValue();
        return challengeService.startResponse(challengeId);
    }


    @RequestMapping(value = "/end", method = RequestMethod.POST)
    public Map end(@RequestBody Map<String, Object> payload) {
        if (!payload.containsKey("challengeId") || !payload.containsKey("questionIdAnswerIdMap")) {
            throw new IllegalArgumentException();
        }
        Long challengeId = ((Integer) payload.get("challengeId")).longValue();
        Map<String, Integer> questionIdAnswerIdMap = (Map<String, Integer>) payload.get("questionIdAnswerIdMap");
        return challengeService.end(challengeId, questionIdAnswerIdMap);
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public List<ChallengeInfoDTO> list() {
        return challengeService.list();
    }

    @RequestMapping(value = "/summary", method = RequestMethod.POST)
    public ChallengeSummaryDTO summary(@RequestBody Map<String, Object> payload){
        if (!payload.containsKey("challengeId")) {
            throw new IllegalArgumentException();
        }
        Long challengeId = ((Integer) payload.get("challengeId")).longValue();
        return challengeService.summary(challengeId);
    }
}
