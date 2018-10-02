package com.ww.model.container.rival.war;

import com.ww.model.constant.Skill;
import com.ww.model.dto.rival.SkillDTO;
import com.ww.model.entity.outside.wisie.OwnedWisie;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public boolean canUseHint() {
        return hints > 0;
    }

    public void useHint() {
        hints--;
    }

    public boolean canUseWaterPistol() {
        return waterPistols > 0;
    }

    public void useWaterPistol() {
        waterPistols--;
    }

    public boolean canUseLifebuoy() {
        return lifebuoys > 0;
    }

    public void useLifebuoy() {
        lifebuoys--;
    }


    public Map<String, Integer> prepareSkills() {
        Map<String,Integer> skills = new HashMap<>();
        skills.put(Skill.HINT.name(),hints);
        skills.put(Skill.WATER_PISTOL.name(),waterPistols);
        skills.put(Skill.LIFEBUOY.name(),lifebuoys);
        return skills;
//        List<SkillDTO> skills = new ArrayList<>();
//        if (canUseHint()) {
//            skills.add(new SkillDTO(Skill.HINT, hints));
//        }
//        if (canUseWaterPistol()) {
//            skills.add(new SkillDTO(Skill.WATER_PISTOL, waterPistols));
//        }
//        if (canUseLifebuoy()) {
//            skills.add(new SkillDTO(Skill.LIFEBUOY, lifebuoys));
//        }
//        return skills;
    }
}
