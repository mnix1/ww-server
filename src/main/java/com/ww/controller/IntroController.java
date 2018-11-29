package com.ww.controller;

import com.ww.service.social.IntroService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "/intro")
@AllArgsConstructor
public class IntroController {

    private final IntroService introService;

    @RequestMapping(value = "/changeStepIndex", method = RequestMethod.POST)
    public Map changeStepIndex(@RequestBody Map<String, Object> payload) {
        if (!payload.containsKey("stepIndex")) {
            throw new IllegalArgumentException();
        }
        Integer stepIndex = (Integer) payload.get("stepIndex");
        return introService.changeIntroStepIndex(stepIndex);
    }

    @RequestMapping(value = "/pickWisies", method = RequestMethod.POST)
    public Map pickWisies(@RequestBody Map<String, Object> payload) {
        if (!payload.containsKey("wisieTypes")) {
            throw new IllegalArgumentException();
        }
        List<String> wisieTypes = (List<String>) payload.get("wisieTypes");
        return introService.pickWisies(null, wisieTypes);
    }

}
