package com.ww.model.container.rival.war;

import com.ww.model.entity.outside.wisie.OwnedWisie;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class WarTeamSkills {

    private int hints;
    private int waterPistols;
    private int lifebuoys;

    public WarTeamSkills(int base) {
        this.hints = base;
        this.waterPistols = base;
        this.lifebuoys = base;
    }

    public WarTeamSkills(int base, List<? extends OwnedWisie> wisies) {
        this.hints = base;
        this.waterPistols = base;
        this.lifebuoys = base;
    }

    public void decreaseHints() {
        hints--;
    }

    public void decreaseWaterPistols() {
        waterPistols--;
    }

    public void decreaseLifebuoys() {
        lifebuoys--;
    }

}
