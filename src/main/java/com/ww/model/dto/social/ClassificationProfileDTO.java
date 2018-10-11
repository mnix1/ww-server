package com.ww.model.dto.social;

import com.ww.model.entity.outside.rival.season.ProfileSeason;
import lombok.Getter;

@Getter
public class ClassificationProfileDTO extends RivalProfileDTO {

    private Long position;

    public ClassificationProfileDTO(ProfileSeason profileSeason, Long position) {
        super(profileSeason);
        this.position = position;
    }
}
