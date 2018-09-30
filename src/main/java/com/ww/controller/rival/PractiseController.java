package com.ww.controller.rival;

import com.ww.model.constant.Category;
import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.service.rival.PractiseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(value = "/practise")
public class PractiseController {
    @Autowired
    private PractiseService practiseService;

    @RequestMapping(value = "/start", method = RequestMethod.POST)
    public Map start(@RequestBody Map<String, Object> payload) {
        if (!payload.containsKey("category") || !payload.containsKey("difficultyLevel")) {
            throw new IllegalArgumentException();
        }
        Map<String, Object> model = new HashMap<>();
        Category category = Category.fromString((String) payload.get("category"));
        DifficultyLevel difficultyLevel = DifficultyLevel.fromString((String) payload.get("difficultyLevel"));
        model.put("practise", practiseService.start(category, difficultyLevel));
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
