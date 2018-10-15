package com.ww.model.dto.social;

import com.ww.model.entity.outside.social.Profile;
import lombok.Getter;

@Getter
public class ExtendedProfileDTO extends BaseProfileDTO {

    private String tag;

    public ExtendedProfileDTO(Profile profile) {
        super(profile);
        this.tag = profile.getTag();
    }
}
