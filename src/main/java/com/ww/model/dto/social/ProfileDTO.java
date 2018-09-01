package com.ww.model.dto.social;

import com.ww.model.entity.social.Profile;
import lombok.Getter;

@Getter
public class ProfileDTO {

    private String tag;
    private String name;
    private Long level;
    private String wisorType;
    private Boolean teamInitialized;

    public ProfileDTO(Profile profile) {
        this.tag = profile.getTag();
        this.name = profile.getName();
        this.level = profile.getLevel();
        this.wisorType = profile.getWisorType();
        this.teamInitialized = profile.getTeamInitialized();
    }
}
