package com.ww.model.dto.social;

import com.ww.model.constant.Grade;
import com.ww.model.constant.rival.RivalType;
import com.ww.model.container.Resources;
import com.ww.model.entity.outside.rival.season.ProfileSeason;
import com.ww.model.entity.outside.rival.season.SeasonGrade;
import lombok.Getter;

@Getter
public class SeasonGradeDTO {

    protected Grade grade;
    private Long rangeFrom;
    private Resources rewards;

    public SeasonGradeDTO(SeasonGrade seasonGrade) {
        this.grade = seasonGrade.getGrade();
        this.rangeFrom = seasonGrade.getRangeFrom();
        this.rewards = seasonGrade.getResources();
    }
}
