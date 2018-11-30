package com.ww.model.dto.social;

import com.ww.model.entity.outside.social.ProfileIntro;
import lombok.Getter;

@Getter
public class ProfileIntroDTO {

    private Integer introductionStepIndex;

    public ProfileIntroDTO(ProfileIntro intro) {
        this.introductionStepIndex = intro.getIntroductionStepIndex();
    }
}
