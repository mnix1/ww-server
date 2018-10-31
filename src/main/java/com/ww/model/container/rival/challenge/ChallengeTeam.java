package com.ww.model.container.rival.challenge;

import com.ww.model.container.rival.RivalTeamSkills;
import com.ww.model.container.rival.war.TeamMember;
import com.ww.model.container.rival.war.WarTeam;
import com.ww.model.entity.outside.social.Profile;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public class ChallengeTeam extends WarTeam {
    private Integer score = 0;

    public ChallengeTeam(Profile profile, List<TeamMember> teamMembers, RivalTeamSkills teamSkills) {
        super(profile, teamMembers, teamSkills);
    }

    public void increaseScore(){
        score++;
    }


}
