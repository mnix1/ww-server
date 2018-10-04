package com.ww.model.container.rival;

import com.ww.model.constant.Skill;
import com.ww.model.container.rival.war.skill.available.AvailableSkill;

import java.util.Map;

public interface RivalTeamSkills {
    Map<Skill, AvailableSkill> getSkills();

    boolean canUse(Skill skill);

    AvailableSkill use(Skill skill);

    void blockAll();
    void unblockAll();
    void resetAll();
}
