package com.ww.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/bot")
public class BotController {

    @RequestMapping(value = "/elo", method = RequestMethod.GET)
    public String eli() {
        return "elo";
    }
}
