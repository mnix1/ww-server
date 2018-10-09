package com.ww.model.container.rival.war.skill;

import com.ww.model.constant.Skill;
import com.ww.model.constant.SkillType;
import com.ww.model.container.rival.RivalTeamSkills;
import com.ww.model.container.rival.war.TeamMember;
import com.ww.model.container.rival.war.WarWisie;
import com.ww.model.container.rival.war.WisieTeamMember;
import com.ww.model.container.rival.war.skill.available.AvailableSkill;
import com.ww.model.container.rival.war.skill.available.SkillBuilder;
import com.ww.model.container.rival.war.skill.available.active.ActiveAvailableSkill;
import com.ww.model.container.rival.war.skill.available.passive.PassiveAvailableSkill;
import com.ww.model.entity.outside.wisie.OwnedWisie;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class WarTeamSkills implements RivalTeamSkills {

    private Map<Skill, AvailableSkill> skills = new ConcurrentHashMap<>();
    private Map<Skill, ActiveAvailableSkill> activeSkills = new ConcurrentHashMap<>();
    private Map<Skill, PassiveAvailableSkill> passiveSkills = new ConcurrentHashMap<>();

    public WarTeamSkills(int count, List<? extends TeamMember> teamMembers) {
        this.activeSkills.put(Skill.HINT, (ActiveAvailableSkill) SkillBuilder.build(Skill.HINT, count));
        this.activeSkills.put(Skill.WATER_PISTOL, (ActiveAvailableSkill) SkillBuilder.build(Skill.WATER_PISTOL, count));
        this.activeSkills.put(Skill.LIFEBUOY, (ActiveAvailableSkill) SkillBuilder.build(Skill.LIFEBUOY, count));
        this.activeSkills.put(Skill.PIZZA, (ActiveAvailableSkill) SkillBuilder.build(Skill.PIZZA, count));
        this.activeSkills.put(Skill.GHOST, (ActiveAvailableSkill) SkillBuilder.build(Skill.GHOST, count));
        initFromWisies(teamMembers);
        skills.putAll(activeSkills);
        skills.putAll(passiveSkills);
    }

    private void initFromWisies(List<? extends TeamMember> teamMembers) {
        for (TeamMember teamMember : teamMembers) {
            if (!teamMember.isWisie()) {
                continue;
            }
            WisieTeamMember wisieTeamMember = (WisieTeamMember) teamMember;
            WarWisie warWisie = wisieTeamMember.getContent();
            OwnedWisie wisie = warWisie.getWisie();
            for (Skill skill : wisie.getSkills()) {
                if (activeSkills.containsKey(skill)) {
                    activeSkills.get(skill).increaseCount();
                } else if (passiveSkills.containsKey(skill)) {
                    passiveSkills.get(skill).increaseCount();
                } else {
                    AvailableSkill availableSkill = SkillBuilder.build(skill, 1);
                    if (availableSkill.getType() == SkillType.ACTIVE) {
                        activeSkills.put(skill, (ActiveAvailableSkill) availableSkill);
                    } else if (availableSkill.getType() == SkillType.PASSIVE) {
                        PassiveAvailableSkill passiveAvailableSkill = (PassiveAvailableSkill) availableSkill;
                        passiveAvailableSkill.addSourceWarWisie(warWisie);
                        passiveSkills.put(skill, passiveAvailableSkill);
                    }
                }
            }
        }
    }

    @Override
    public boolean canUse(Skill skill) {
        return activeSkills.containsKey(skill) && activeSkills.get(skill).getCanUse();
    }

    @Override
    public ActiveAvailableSkill use(Skill skill) {
        return activeSkills.get(skill).use();
    }

    @Override
    public void blockAll() {
        activeSkills.forEach((skill, availableSkill) -> availableSkill.disable());
    }

    @Override
    public void unblockAll() {
        activeSkills.forEach((skill, availableSkill) -> availableSkill.enable());
    }

    @Override
    public void resetUsedAll() {
        activeSkills.forEach((skill, availableSkill) -> availableSkill.reset());
    }

}
