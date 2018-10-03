package com.ww.model.container.rival;

import com.ww.model.constant.Skill;

import java.util.HashMap;
import java.util.Map;

public class EmptyTeamSkills implements RivalTeamSkills {


    @Override
    public Map<Skill, Integer> getSkills() {
        return new HashMap<>();
    }

    @Override
    public boolean canUseSkill(Skill skill) {
        return false;
    }

    @Override
    public void useSkill(Skill skill) {
    }
}
