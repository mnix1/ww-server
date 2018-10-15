package com.ww.model.dto.social;

import com.ww.model.entity.outside.rival.season.ProfileSeason;
import lombok.Getter;

@Getter
public class ClassificationPositionDTO extends RivalProfileSeasonDTO {

    private Long position;
    private BaseProfileDTO profile;
    private Boolean me;

    public ClassificationPositionDTO(ProfileSeason profileSeason, Long position, String requesterTag) {
        super(profileSeason);
        this.position = position;
        this.profile = new BaseProfileDTO(profileSeason.getProfile());
        this.me = profileSeason.getProfile().getTag().equals(requesterTag);
    }
}
