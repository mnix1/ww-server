package com.ww.model.container.rival.war.skill.available.active;

import com.ww.model.constant.Skill;
import com.ww.model.container.rival.war.skill.available.AvailableSkill;
import lombok.Getter;

@Getter
public class LifebuoyActiveAvailableSkill extends ActiveAvailableSkill {

    public LifebuoyActiveAvailableSkill(int count) {
        super(Skill.LIFEBUOY, count);
    }

    public boolean getCanUse() {
        return !disabled && count > 0;
    }
}
