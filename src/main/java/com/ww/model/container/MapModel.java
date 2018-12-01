package com.ww.model.container;

import com.ww.helper.JSONHelper;

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

    public MapModel(Map<String, Object> model) {
        this.model = model;
    }

    public MapModel put(String key, Object value) {
        model.put(key, value);
        return this;
    }

    public Map<String, Object> get() {
        return model;
    }

    @Override
    public String toString() {
        return JSONHelper.toJSON(model);
    }
}
