package com.ww.model.container.rival.war.skill.available.passive;

import com.ww.model.constant.Skill;
import com.ww.model.constant.SkillType;
import com.ww.model.container.rival.war.WarWisie;
import com.ww.model.container.rival.war.skill.available.AvailableSkill;

import java.util.ArrayList;
import java.util.List;

public class PassiveAvailableSkill extends AvailableSkill {

    public PassiveAvailableSkill(Skill skill, int count) {
        super(skill, count);
    }

    private List<WarWisie> sourceWarWisies = new ArrayList<>();

    public List<WarWisie> listSourceWarWisies() {
        return sourceWarWisies;
    }

    public void addSourceWarWisie(WarWisie warWisie) {
        sourceWarWisies.add(warWisie);
    }

    public boolean getCanUse() {
        return true;
    }

    @Override
    public SkillType getType() {
        return SkillType.PASSIVE;
    }
}
