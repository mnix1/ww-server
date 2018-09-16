package com.ww.model.dto.social;

import com.ww.model.constant.rival.RivalType;
import com.ww.model.entity.social.Profile;
import lombok.Getter;

@Getter
public class ClassificationProfileDTO extends RivalProfileDTO {

    private Long position;

    public ClassificationProfileDTO(Profile profile, RivalType rivalType, Long position) {
        super(profile, rivalType);
        this.position = position;
    }
}
