package com.ww.model.entity.outside.rival.season;

import com.ww.model.constant.Grade;
import com.ww.model.constant.rival.RivalType;
import com.ww.model.container.Resources;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class SeasonGrade {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    protected Grade grade;
    private RivalType type;
    private Long rangeFrom;
    private Long rangeTo;
    private Long goldGain = 0L;
    private Long crystalGain = 0L;
    private Long wisdomGain = 0L;
    private Long elixirGain = 0L;

    public SeasonGrade(Grade grade, RivalType type, Long rangeFrom, Long rangeTo, Resources resources) {
        this.grade = grade;
        this.type = type;
        this.rangeFrom = rangeFrom;
        this.rangeTo = rangeTo;
        this.goldGain = resources.getGold();
        this.crystalGain = resources.getCrystal();
        this.wisdomGain = resources.getWisdom();
        this.elixirGain = resources.getElixir();
    }

    public Resources getResources() {
        return new Resources(goldGain, crystalGain, wisdomGain, elixirGain);
    }
}
