package com.ww.model.dto.social;

import com.ww.model.constant.wisie.WisorType;
import com.ww.model.entity.outside.social.Profile;
import lombok.Getter;

@Getter
public class BaseProfileDTO {

    private String name;
    private WisorType wisorType;

    public BaseProfileDTO(Profile profile) {
        this.name = profile.getName();
        this.wisorType = profile.getWisorType();
    }
}
