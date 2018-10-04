package com.ww.model.container.rival.war.skill.available;

import lombok.Getter;

@Getter
public class LifebuoyAvailableSkill extends AvailableSkill {

    public LifebuoyAvailableSkill(int count) {
        super(count);
    }

    public boolean getCanUse() {
        return !disabled && count > 0;
    }
}
