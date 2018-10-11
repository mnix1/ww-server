package com.ww.model.dto.social;

import com.ww.model.constant.Grade;
import com.ww.model.constant.rival.RivalType;
import com.ww.model.entity.outside.rival.season.ProfileSeason;
import com.ww.model.entity.outside.social.Profile;
import lombok.Getter;

@Getter
public class RivalProfileDTO extends ProfileDTO {

    private Long elo;
    private Grade grade;
    private RivalType type;

    public RivalProfileDTO(ProfileSeason profileSeason) {
        super(profileSeason.getProfile());
        this.elo = profileSeason.getElo();
        this.grade = Grade.fromElo(elo);
        this.type = profileSeason.getSeason().getType();
    }
}
