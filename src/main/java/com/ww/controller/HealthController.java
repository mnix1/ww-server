package com.ww.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

import static com.ww.helper.ModelHelper.putSuccessCode;

@RestController
public class HealthController {

    @RequestMapping("/health")
    public Map<String, Object> health() {
        return putSuccessCode(new HashMap<>());
    }
}
