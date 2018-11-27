package com.ww.model.container.rival.war.skill;

import com.ww.model.constant.Skill;
import com.ww.model.container.rival.war.TeamMember;
import com.ww.model.container.rival.war.WarTeam;
import com.ww.model.container.rival.war.WarWisie;
import com.ww.model.container.rival.war.WisieTeamMember;
import com.ww.model.container.rival.war.skill.available.passive.PassiveAvailableSkill;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PassiveSkillsInit {

    private WarTeam warTeam;
    private List<WisieTeamMember> wisieTeamMembers;

    public PassiveSkillsInit(WarTeam warTeam) {
        this.warTeam = warTeam;
    }

    public void init() {
        if (warTeam.getTeamSkills().getSkills().isEmpty()) {
            return;
        }
        Map<Skill, PassiveAvailableSkill> teamSkills = ((WarTeamSkills) warTeam.getTeamSkills()).getPassiveSkills();
        if (teamSkills.isEmpty()) {
            return;
        }
        wisieTeamMembers = warTeam.getTeamMembers().stream().filter(TeamMember::isWisie).map(tM -> (WisieTeamMember) tM).collect(Collectors.toList());
        for (Map.Entry<Skill, PassiveAvailableSkill> entry : teamSkills.entrySet()) {
            if (entry.getKey() == Skill.TEACHER) {
                initTeacher(entry.getValue());
            } else if (entry.getKey() == Skill.MOTIVATOR) {
                initMotivator(entry.getValue());
            }
        }
    }

    private void initTeacher(PassiveAvailableSkill skill) {
        for (WarWisie sourceWarWisie : skill.listSourceWarWisies()) {
            for (WisieTeamMember teamMember : wisieTeamMembers) {
                teamMember.increaseWisdomAttributes(sourceWarWisie, 0.25);
            }
        }
    }

    private void initMotivator(PassiveAvailableSkill skill) {
        for (WarWisie sourceWarWisie : skill.listSourceWarWisies()) {
            for (WisieTeamMember teamMember : wisieTeamMembers) {
                teamMember.increaseMentalAttributes(sourceWarWisie, 0.25);
            }
        }
    }
}
