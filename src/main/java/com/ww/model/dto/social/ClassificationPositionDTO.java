package com.ww.model.dto.social;

import com.ww.model.entity.outside.rival.season.ProfileSeason;
import lombok.Getter;

@Getter
public class ClassificationPositionDTO extends RivalProfileSeasonDTO {

    private Long position;
    private ProfileDTO profile;

    public ClassificationPositionDTO(ProfileSeason profileSeason, Long position) {
        super(profileSeason);
        this.position = position;
        this.profile = new ProfileDTO(profileSeason.getProfile());
    }
}
