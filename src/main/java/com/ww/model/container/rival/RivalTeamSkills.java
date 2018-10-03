package com.ww.model.container.rival;

import com.ww.model.constant.Skill;

import java.util.Map;

public interface RivalTeamSkills {
    Map<Skill, Integer> getSkills();

    boolean canUseSkill(Skill skill);

    void useSkill(Skill skill);
}
