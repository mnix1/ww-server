package com.ww.controller;

import lombok.AllArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/cache")
@AllArgsConstructor
public class CacheController {

    private final CacheManager cacheManager;

    @RequestMapping(value = "/clear", method = RequestMethod.GET)
    public void clearCache() {
        for (String name : cacheManager.getCacheNames()) {
            cacheManager.getCache(name).clear();
        }
    }
}
