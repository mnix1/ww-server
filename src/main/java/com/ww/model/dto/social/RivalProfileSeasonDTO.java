package com.ww.model.dto.social;

import com.ww.model.constant.Grade;
import com.ww.model.constant.rival.RivalType;
import com.ww.model.entity.outside.rival.season.ProfileSeason;
import com.ww.model.entity.outside.social.Profile;
import lombok.Getter;

@Getter
public class RivalProfileSeasonDTO {

    private Long elo;
    private Long previousElo;
    private Grade grade;

    public RivalProfileSeasonDTO(ProfileSeason profileSeason) {
        this.elo = profileSeason.getElo();
        this.previousElo = profileSeason.getPreviousElo();
        this.grade = Grade.fromElo(elo);
    }
}
