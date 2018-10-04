package com.ww.model.container.rival.war.skill;

import com.ww.model.constant.Skill;
import com.ww.model.container.rival.RivalTeamSkills;
import com.ww.model.container.rival.war.skill.available.AvailableSkill;

import java.util.HashMap;
import java.util.Map;

public class EmptyTeamSkills implements RivalTeamSkills {


    @Override
    public Map<Skill, AvailableSkill> getSkills() {
        return new HashMap<>();
    }

    @Override
    public boolean canUse(Skill skill) {
        return false;
    }

    @Override
    public AvailableSkill use(Skill skill) {
        return null;
    }

    @Override
    public void blockAll() {
    }

    @Override
    public void unblockAll() {
    }

    @Override
    public void resetAll() {
    }
}
