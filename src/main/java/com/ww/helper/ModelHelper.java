package com.ww.helper;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ModelHelper {
    public static Map<String, Object> putErrorCode(Map<String, Object> model) {
        return putCode(model, -1);
    }

    public static Map<String, Object> putSuccessCode(Map<String, Object> model) {
        return putCode(model, 1);
    }

    public static Map<String, Object> putCode(Map<String, Object> model, int code) {
        model.put("code", code);
        return model;
    }

    public static boolean success(Map<String, Object> model) {
        if (!model.containsKey("code")) {
            return false;
        }
        return ((int) model.get("code")) == 1;
    }

    public static Map<String, Object> parseMessage(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(json, HashMap.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new HashMap<>();
    }
}
