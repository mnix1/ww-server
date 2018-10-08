package com.ww.model.container.rival;

import com.ww.model.constant.Skill;
import com.ww.model.container.rival.war.WarTeam;
import com.ww.model.container.rival.war.skill.available.AvailableSkill;
import com.ww.model.container.rival.war.skill.available.active.ActiveAvailableSkill;

import java.util.Map;

public interface RivalTeamSkills {
    Map<Skill, AvailableSkill> getSkills();

    boolean canUse(Skill skill);

    ActiveAvailableSkill use(Skill skill);

    void blockAll();
    void unblockAll();
    void resetUsedAll();
}
