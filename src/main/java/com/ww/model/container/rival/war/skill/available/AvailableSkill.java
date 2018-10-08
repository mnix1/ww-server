package com.ww.model.container.rival.war.skill.available;

import com.ww.model.constant.Skill;
import com.ww.model.constant.SkillType;
import lombok.Getter;

public abstract class AvailableSkill {

    @Getter
    protected int count;
    protected Skill skill;

    public abstract SkillType getType();

    public AvailableSkill(Skill skill, int count) {
        this.skill = skill;
        this.count = count;
    }

    public void increaseCount() {
        count++;
    }
}
