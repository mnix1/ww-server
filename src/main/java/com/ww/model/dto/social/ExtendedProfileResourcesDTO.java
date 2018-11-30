package com.ww.model.dto.social;

import com.ww.model.constant.Language;
import com.ww.model.container.Resources;
import com.ww.model.entity.outside.social.Profile;
import lombok.Getter;

@Getter
public class ExtendedProfileResourcesDTO extends ExtendedProfileDTO {

    private Long level;
    private Long experience;
    private Resources resources;
    private Language language;
    private ProfileIntroDTO intro;

    public ExtendedProfileResourcesDTO(Profile profile) {
        super(profile);
        this.language = profile.getLanguage();
        this.level = profile.getLevel();
        this.experience = profile.getExperience();
        this.resources = profile.getResources();
        this.intro = new ProfileIntroDTO(profile.getIntro());
    }
}
