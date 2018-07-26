package com.ww.controller.rival;

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
    public Map startFriend(@RequestBody Map<String, Object> payload) {
        List<String> tags = (List<String>) payload.get("tags");
        Map<String, Object> model = new HashMap<>();
        model.put("battle", battleService.startFriend(tags));
        return model;
    }
}
