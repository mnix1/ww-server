package com.ww.helper;

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
}
