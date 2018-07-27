package com.ww.controller.rival;

import com.ww.model.dto.task.BattleDTO;
import com.ww.service.SessionService;
import com.ww.service.rival.BattleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/battle")
public class BattleController {

    @Autowired
    SessionService sessionService;

    @Autowired
    BattleService battleService;

    @RequestMapping(value = "/startFriend", method = RequestMethod.POST)
    public BattleDTO startFriend(@RequestBody Map<String, Object> payload) {
        if (!payload.containsKey("tags")) {
            throw new IllegalArgumentException();
        }
        List<String> tags = (List<String>) payload.get("tags");
        return battleService.startFriend(tags);
    }

    @RequestMapping(value = "/endFriend", method = RequestMethod.POST)
    public Map endFriend(@RequestBody Map<String, Object> payload) {
        if (!payload.containsKey("battleId") || !payload.containsKey("questionIdAnswerIdMap")) {
            throw new IllegalArgumentException();
        }
        Long battleId = ((Integer) payload.get("battleId")).longValue();
        Map<String, Integer> questionIdAnswerIdMap = (Map<String, Integer>) payload.get("questionIdAnswerIdMap");
        return battleService.endFriend(battleId, questionIdAnswerIdMap);
    }
}
