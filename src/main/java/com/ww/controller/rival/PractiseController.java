package com.ww.controller.rival;

import com.ww.model.constant.Category;
import com.ww.service.rival.BattleService;
import com.ww.service.rival.PractiseService;
import com.ww.service.SessionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/practise")
public class PractiseController {
    @Autowired
    SessionService sessionService;

    @Autowired
    PractiseService practiseService;

    @RequestMapping(value = "/start", method = RequestMethod.POST)
    public Map start(@RequestBody Map<String, Object> payload) {
        if (!payload.containsKey("category")) {
            throw new IllegalArgumentException();
        }
        Map<String, Object> model = new HashMap<>();
        Category category = Category.fromString((String) payload.get("category"));
        model.put("practise", practiseService.start(Category.mapToNotRandom(category)));
        return model;
    }

    @RequestMapping(value = "/end", method = RequestMethod.POST)
    public Map end(@RequestBody Map<String, Object> payload) {
        if (!payload.containsKey("practiseId") || !payload.containsKey("answerId")) {
            throw new IllegalArgumentException();
        }
        Integer practiseId = (Integer) payload.get("practiseId");
        Integer answerId = (Integer) payload.get("answerId");
        return practiseService.end(practiseId.longValue(), answerId.longValue());
    }
}
