package com.ww.controller;

import com.ww.model.container.MapModel;
import com.ww.service.auto.AutoService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequestMapping(value = "/_auto")
@RestController
@AllArgsConstructor
public class AutoController {

    @RequestMapping(value = "/changeMaxManagers", method = RequestMethod.GET)
    public Map<String, Object> changeMaxManagers(@RequestParam int max) {
        int current = AutoService.MAX_ACTIVE_AUTO_MANAGERS;
        AutoService.MAX_ACTIVE_AUTO_MANAGERS = max;
        return new MapModel("was", current).put("now", max).get();
    }
}
