package com.ww.model.dto.social;

import com.ww.model.dto.DTO;
import com.ww.model.entity.outside.social.Profile;
import lombok.Getter;

@Getter
public class ExperienceDTO extends DTO {

    private Long experience;
    private Long level;
    private long experienceGain;
    private long levelGain;

    public ExperienceDTO(Profile profile, long experienceGain, long levelGain) {
        this.experience = profile.getExperience();
        this.level = profile.getLevel();
        this.experienceGain = experienceGain;
        this.levelGain = levelGain;
    }
}
