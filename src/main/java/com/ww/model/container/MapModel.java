package com.ww.model.container;

import java.util.HashMap;
import java.util.Map;

public class MapModel {
    private Map<String, Object> model;

    public MapModel() {
        model = new HashMap<>();
    }

    public MapModel(String key, Object value) {
        this();
        put(key, value);
    }

    public MapModel put(String key, Object value) {
        model.put(key, value);
        return this;
    }

    public Map<String, Object> get() {
        return model;
    }

}
