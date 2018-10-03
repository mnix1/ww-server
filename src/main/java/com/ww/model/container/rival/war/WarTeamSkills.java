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
public class WarTeamSkills implements RivalTeamSkills {

    private Map<Skill, Integer> skills = new HashMap<>();

    public WarTeamSkills(int base, List<? extends OwnedWisie> wisies) {
        this.skills.put(Skill.HINT, base);
        this.skills.put(Skill.WATER_PISTOL, base);
        this.skills.put(Skill.LIFEBUOY, base);
//        this.skills.put(Skill.BLOCK, base);
//        this.skills.put(Skill.TASK_CHANGE, base);
        this.skills.put(Skill.KIDNAPPING, base);
        initSkills(wisies);
    }

    private void initSkills(List<? extends OwnedWisie> wisies) {
    }

    @Override
    public boolean canUseSkill(Skill skill) {
        return skills.containsKey(skill) && skills.get(skill) != 0;
    }

    @Override
    public void useSkill(Skill skill) {
        int value = skills.get(skill);
        skills.remove(skill);
        skills.put(skill, value - 1);
    }
}
