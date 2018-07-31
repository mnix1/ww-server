package com.ww.controller.rival;

import com.ww.service.SessionService;
import com.ww.service.rival.BattleService;
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
    BattleService battleService;

    @RequestMapping(value = "/start", method = RequestMethod.POST)
    public Map start(@RequestBody Map<String, Object> payload) {
        String tag = null;
        if (payload.containsKey("tag")) {
            tag = (String) payload.get("tag");
        }
        return battleService.start(tag);
    }

    @RequestMapping(value = "/cancel", method = RequestMethod.POST)
    public Map cancel() {
        return battleService.cancel();
    }

    @RequestMapping(value = "/accept", method = RequestMethod.POST)
    public Map accept(@RequestBody Map<String, Object> payload) {
        if (!payload.containsKey("tag")) {
            throw new IllegalArgumentException();
        }
        return battleService.start((String) payload.get("tag"));
    }

    @RequestMapping(value = "/reject", method = RequestMethod.POST)
    public Map reject(@RequestBody Map<String, Object> payload) {
        if (!payload.containsKey("tag")) {
            throw new IllegalArgumentException();
        }
        return battleService.start((String) payload.get("tag"));
    }


}
