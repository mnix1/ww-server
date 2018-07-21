package com.ww.controller.rival;

import com.ww.model.constant.Category;
import com.ww.service.rival.PractiseService;
import com.ww.service.SessionService;
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

    @RequestMapping(value = "/start", method = RequestMethod.GET)
    public Map start(@RequestParam Category category) {
        Map<String, Object> model = new HashMap<>();
        model.put("practise", practiseService.start(Category.mapToNotRandom(category)));
        return model;
    }

    @RequestMapping(value = "/end", method = RequestMethod.GET)
    public Map end(@RequestParam Long practiseId, @RequestParam Long answerId) {
        Map<String, Object> model = new HashMap<>();
        Long correctAnswerId = practiseService.end(practiseId, answerId);
        if (correctAnswerId != null) {
            model.put("correctAnswerId", correctAnswerId);
        }
        return model;
    }
}
