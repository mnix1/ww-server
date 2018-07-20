package com.ww.controller.rival;

import com.ww.service.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class BattleController {

    @Autowired
    SessionService sessionService;

    @RequestMapping(value = "/startTraining", method = RequestMethod.GET)
    public Map startTraining() {
        Map<String, Long> model = new HashMap<>();
        return model;
    }
}
