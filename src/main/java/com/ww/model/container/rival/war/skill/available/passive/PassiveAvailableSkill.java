package com.ww.model.container.rival.war.skill.available.passive;

import com.ww.model.constant.Skill;
import com.ww.model.constant.SkillType;
import com.ww.model.container.rival.war.skill.available.AvailableSkill;
import lombok.Getter;

@Getter
public class PassiveAvailableSkill extends AvailableSkill {

    public PassiveAvailableSkill(Skill skill, int count) {
        super(skill, count);
    }

    @Override
    public SkillType getType() {
        return SkillType.PASSIVE;
    }
}
