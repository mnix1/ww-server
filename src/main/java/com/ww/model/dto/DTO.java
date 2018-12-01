package com.ww.model.dto;

import com.ww.helper.JSONHelper;

public class DTO {
    @Override
    public String toString() {
        return JSONHelper.toJSON(this);
    }
}
