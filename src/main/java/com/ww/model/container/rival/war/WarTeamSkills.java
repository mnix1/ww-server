package com.ww.model.container.rival.war;

import com.ww.model.constant.Skill;
import com.ww.model.container.rival.RivalTeamSkills;
import com.ww.model.entity.outside.wisie.OwnedWisie;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class WarTeamSkills implements RivalTeamSkills {

    private int hints;
    private int waterPistols;
    private int lifebuoys;

    public WarTeamSkills(int base, List<? extends OwnedWisie> wisies) {
        this.hints = base;
        this.waterPistols = base;
        this.lifebuoys = base;
    }

    @Override
    public boolean canUseHint() {
        return hints > 0;
    }

    @Override
    public void useHint() {
        hints--;
    }

    @Override
    public boolean canUseWaterPistol() {
        return waterPistols > 0;
    }

    @Override
    public void useWaterPistol() {
        waterPistols--;
    }

    @Override
    public boolean canUseLifebuoy() {
        return lifebuoys > 0;
    }

    @Override
    public void useLifebuoy() {
        lifebuoys--;
    }

    @Override
    public Map<String, Integer> prepareSkills() {
        Map<String, Integer> skills = new HashMap<>();
        skills.put(Skill.HINT.name(), hints);
        skills.put(Skill.WATER_PISTOL.name(), waterPistols);
        skills.put(Skill.LIFEBUOY.name(), lifebuoys);
        return skills;
    }
}
