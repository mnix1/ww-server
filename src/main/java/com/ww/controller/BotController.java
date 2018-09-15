package com.ww.controller;

import com.ww.manager.rival.RivalManager;
import com.ww.service.SessionService;
import com.ww.service.rival.war.WarService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static com.ww.helper.ModelHelper.putSuccessCode;

@RestController
@RequestMapping(value = "/bot")
public class BotController {

    @Autowired
    private WarService warService;

    @Autowired
    private SessionService sessionService;

    @RequestMapping(value = "/auth", method = RequestMethod.GET)
    public Map<String, Object> auth() {
        Map<String, Object> model = new HashMap<>();
        return putSuccessCode(model);
    }

    @RequestMapping(value = "/correctAnswer", method = RequestMethod.GET)
    public Map<String, Object> correctAnswer() {
        Map<String, Object> model = new HashMap<>();
        RivalManager rivalManager = warService.getProfileIdToRivalManagerMap().get(sessionService.getProfileId());
        model.put("id", rivalManager.getRivalContainer().findCurrentCorrectAnswerId());
        return putSuccessCode(model);
    }
}
