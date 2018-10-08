package com.ww.model.container.rival.war.skill;

import com.ww.model.constant.Skill;
import com.ww.model.container.rival.RivalTeamSkills;
import com.ww.model.container.rival.war.skill.available.AvailableSkill;
import com.ww.model.container.rival.war.skill.available.LifebuoyAvailableSkill;
import com.ww.model.entity.outside.wisie.OwnedWisie;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class WarTeamSkills implements RivalTeamSkills {

    private Map<Skill, AvailableSkill> skills = new ConcurrentHashMap<>();

    public WarTeamSkills(int base, List<? extends OwnedWisie> wisies) {
        this.skills.put(Skill.HINT, new AvailableSkill(base));
        this.skills.put(Skill.WATER_PISTOL, new AvailableSkill(base));
        this.skills.put(Skill.LIFEBUOY, new LifebuoyAvailableSkill(base));
        init(wisies);
    }

    private void init(List<? extends OwnedWisie> wisies) {
        for (OwnedWisie wisie : wisies) {
            for (Skill skill : wisie.getSkills()) {
                AvailableSkill availableSkill;
                if (skills.containsKey(skill)) {
                    availableSkill = new AvailableSkill(1 + skills.get(skill).getCount());
                    skills.remove(skill);
                } else {
                    availableSkill = new AvailableSkill(1);
                }
                skills.put(skill, availableSkill);
            }
        }
    }

    @Override
    public boolean canUse(Skill skill) {
        return skills.containsKey(skill) && skills.get(skill).getCanUse();
    }

    @Override
    public AvailableSkill use(Skill skill) {
        return skills.get(skill).use();
    }

    @Override
    public void blockAll() {
        skills.forEach((skill, availableSkill) -> availableSkill.disable());
    }

    @Override
    public void unblockAll() {
        skills.forEach((skill, availableSkill) -> availableSkill.enable());
    }

    @Override
    public void resetUsedAll() {
        skills.forEach((skill, availableSkill) -> availableSkill.reset());
    }

}
