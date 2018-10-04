package com.ww.model.container.rival.war.skill.available;

import lombok.Getter;

@Getter
public class AvailableSkill {

    protected boolean used = false;
    protected boolean disabled = false;
    protected int count;

    public AvailableSkill(int count) {
        this.count = count;
    }

    public boolean getCanUse() {
        return !disabled && !used && count > 0;
    }

    public boolean getUsed() {
        return used;
    }

    public AvailableSkill disable() {
        disabled = true;
        return this;
    }

    public AvailableSkill enable() {
        disabled = false;
        return this;
    }

    public AvailableSkill reset() {
        used = false;
        enable();
        return this;
    }

    public AvailableSkill use() {
        count--;
        used = true;
        return this;
    }
}
