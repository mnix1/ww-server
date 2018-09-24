package com.ww.model.dto.social;

import com.ww.model.constant.wisie.WisorType;
import com.ww.model.entity.outside.social.Profile;
import lombok.Getter;

@Getter
public class ProfileDTO {

    private String tag;
    private String name;
    private WisorType wisorType;

    public ProfileDTO(Profile profile) {
        this.tag = profile.getTag();
        this.name = profile.getName();
        this.wisorType = profile.getWisorType();
    }
}
