package com.ww.controller;

import com.ww.model.entity.social.Profile;
import com.ww.service.social.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping(value = "/intro")
public class IntroController {

    @Autowired
    ProfileService profileService;

    @RequestMapping(value = "/changeStepIndex", method = RequestMethod.POST)
    public Map changeStepIndex(@RequestBody Map<String, Object> payload) {
        if (!payload.containsKey("stepIndex")) {
            throw new IllegalArgumentException();
        }
        Integer stepIndex = (Integer) payload.get("stepIndex");
        return profileService.changeIntroStepIndex(stepIndex);
    }

}
