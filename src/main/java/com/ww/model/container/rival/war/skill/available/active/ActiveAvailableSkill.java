package com.ww.model.container.rival.war.skill.available.active;

import com.ww.model.constant.Skill;
import com.ww.model.constant.SkillType;
import com.ww.model.container.rival.war.skill.available.AvailableSkill;
import lombok.Getter;

@Getter
public class ActiveAvailableSkill extends AvailableSkill {

    protected boolean used = false;
    protected boolean disabled = false;

    public ActiveAvailableSkill(Skill skill, int count) {
        super(skill, count);
    }

    @Override
    public SkillType getType() {
        return SkillType.ACTIVE;
    }

    public boolean getCanUse() {
        return !disabled && !used && count > 0;
    }

    public boolean getUsed() {
        return used;
    }

    public ActiveAvailableSkill disable() {
        disabled = true;
        return this;
    }

    public ActiveAvailableSkill enable() {
        disabled = false;
        return this;
    }

    public ActiveAvailableSkill reset() {
        used = false;
        enable();
        return this;
    }

    public ActiveAvailableSkill use() {
        count--;
        used = true;
        return this;
    }
}
