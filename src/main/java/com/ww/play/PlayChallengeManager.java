package com.ww.play;

import com.ww.helper.TeamHelper;
import com.ww.model.container.rival.RivalTeams;
import com.ww.model.container.rival.init.RivalTwoInit;
import com.ww.model.container.rival.war.TeamMember;
import com.ww.model.container.rival.war.WarInterval;
import com.ww.model.container.rival.war.WarTeam;
import com.ww.model.container.rival.war.skill.WarTeamSkills;
import com.ww.model.entity.outside.social.Profile;
import com.ww.model.entity.outside.wisie.ProfileWisie;
import com.ww.play.communication.PlayWarCommunication;
import com.ww.play.container.PlayWarContainer;
import com.ww.play.flow.PlayWarFlow;
import com.ww.service.rival.war.RivalWarService;

import java.util.List;

public class PlayChallengeManager extends PlayManager {
    public PlayChallengeManager(RivalTwoInit init, RivalWarService rivalService) {
        super(rivalService);
        this.container = new PlayWarContainer(init, prepareTeams(init), prepareTasks(), prepareTimeouts(), prepareDecisions(), prepareResult());
        this.flow = new PlayWarFlow(this, new WarInterval());
        this.communication = new PlayWarCommunication(this);
    }

    protected RivalTeams prepareTeams(RivalTwoInit init) {
        Profile creatorProfile = init.getCreatorProfile();
        Profile opponentProfile = init.getOpponentProfile();
        return new RivalTeams(prepareTeam(creatorProfile), prepareTeam(opponentProfile));
    }

    protected WarTeam prepareTeam(Profile profile) {
        List<ProfileWisie> wisies = ((RivalWarService) rivalService).getProfileWisies(profile);
        List<TeamMember> teamMembers = TeamHelper.prepareTeamMembers(profile, wisies);
        return new WarTeam(profile, teamMembers, new WarTeamSkills(1, teamMembers));
    }

}
