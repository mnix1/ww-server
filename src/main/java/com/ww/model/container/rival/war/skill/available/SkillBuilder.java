package com.ww.model.container.rival.war.skill.available;

import com.ww.model.constant.Skill;
import com.ww.model.container.rival.war.skill.available.active.ActiveAvailableSkill;
import com.ww.model.container.rival.war.skill.available.active.LifebuoyActiveAvailableSkill;
import com.ww.model.container.rival.war.skill.available.passive.PassiveAvailableSkill;
import lombok.Getter;

@Getter
public class SkillBuilder {

    public static AvailableSkill build(Skill skill, int count) {
        if (skill == Skill.LIFEBUOY) {
            return new LifebuoyActiveAvailableSkill(count);
        }
        if (skill == Skill.MOTIVATOR || skill == Skill.TEACHER) {
            return new PassiveAvailableSkill(skill, count);
        }
        return new ActiveAvailableSkill(skill, count);
    }
}
