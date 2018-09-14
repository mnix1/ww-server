package com.ww.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static com.ww.helper.ModelHelper.putSuccessCode;

@RestController
@RequestMapping(value = "/bot")
public class BotController {

    @RequestMapping(value = "/auth", method = RequestMethod.GET)
    public Map<String, Object> auth() {
        Map<String, Object> model = new HashMap<>();
        return putSuccessCode(model);
    }
}
