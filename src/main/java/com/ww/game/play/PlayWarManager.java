package com.ww.game.play;

import com.ww.game.play.communication.PlayWarCommunication;
import com.ww.game.play.container.PlayWarContainer;
import com.ww.game.play.flow.PlayWarFlow;
import com.ww.helper.TeamHelper;
import com.ww.model.container.rival.RivalTeams;
import com.ww.model.container.rival.init.RivalTwoInit;
import com.ww.model.container.rival.war.TeamMember;
import com.ww.model.container.rival.war.WarInterval;
import com.ww.model.container.rival.war.WarTeam;
import com.ww.model.container.rival.war.skill.WarTeamSkills;
import com.ww.model.entity.outside.social.Profile;
import com.ww.model.entity.outside.wisie.ProfileWisie;
import com.ww.service.rival.war.RivalWarService;

import java.util.List;

public class PlayWarManager extends PlayManager {
    public PlayWarManager(RivalTwoInit init, RivalWarService rivalService) {
        super(rivalService);
        this.interval = new WarInterval();
        this.container = new PlayWarContainer(init, prepareTeams(init), prepareTasks(), prepareTimeouts(), prepareDecisions(), prepareResult());
        this.flow = new PlayWarFlow(this);
        this.communication = new PlayWarCommunication(this);
    }

    protected RivalTeams prepareTeams(RivalTwoInit init) {
        return new RivalTeams(prepareTeam(init.getCreatorProfile()), prepareTeam(init.getOpponentProfile()));
    }

    protected WarTeam prepareTeam(Profile profile) {
        List<ProfileWisie> wisies = ((RivalWarService) service).getProfileWisies(profile);
        List<TeamMember> teamMembers = TeamHelper.prepareTeamMembers(profile, wisies);
        return new WarTeam(profile, teamMembers, new WarTeamSkills(1, teamMembers));
    }

}
