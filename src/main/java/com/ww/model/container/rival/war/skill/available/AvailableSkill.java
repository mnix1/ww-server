package com.ww.model.container.rival.war.skill.available;

import com.ww.model.constant.Skill;
import lombok.Getter;

import java.util.Map;

@Getter
public class AvailableSkill {

    protected boolean disabled = false;
    protected int count;

    public AvailableSkill(int count) {
        this.count = count;
    }

    public boolean getCanUse() {
        return !disabled && count > 0;
    }

    public AvailableSkill disable() {
        disabled = true;
        return this;
    }

    public AvailableSkill enable() {
        disabled = false;
        return this;
    }

    public AvailableSkill use() {
        count--;
        return this;
    }

    public AvailableSkill use(Map<Skill, AvailableSkill> skills) {
        use();
        return this;
    }
}
