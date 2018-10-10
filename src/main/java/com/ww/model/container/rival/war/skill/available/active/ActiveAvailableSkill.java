package com.ww.model.container.rival.war.skill.available.active;

import com.ww.model.constant.Skill;
import com.ww.model.constant.SkillType;
import com.ww.model.container.rival.war.skill.available.AvailableSkill;

public class ActiveAvailableSkill extends AvailableSkill {

    protected boolean used = false;
    protected int disabled = 0;

    public ActiveAvailableSkill(Skill skill, int count) {
        super(skill, count);
    }

    @Override
    public SkillType getType() {
        return SkillType.ACTIVE;
    }

    public boolean getCanUse() {
        return disabled == 0 && !used && count > 0;
    }

    public boolean getUsed() {
        return used;
    }

    public ActiveAvailableSkill disable() {
        disabled++;
        return this;
    }

    public ActiveAvailableSkill enable() {
        disabled = Math.max(disabled - 1, 0);
        return this;
    }

    public ActiveAvailableSkill reset() {
        used = false;
        disabled = 0;
        return this;
    }

    public ActiveAvailableSkill use() {
        count--;
        used = true;
        return this;
    }
}
