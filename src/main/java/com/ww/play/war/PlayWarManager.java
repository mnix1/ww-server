package com.ww.play.war;

import com.ww.helper.TeamHelper;
import com.ww.model.container.rival.init.RivalTwoPlayerInit;
import com.ww.model.container.rival.war.TeamMember;
import com.ww.model.container.rival.war.WarInterval;
import com.ww.model.container.rival.war.WarTeam;
import com.ww.model.container.rival.war.WarTeams;
import com.ww.model.container.rival.war.skill.WarTeamSkills;
import com.ww.model.entity.outside.social.Profile;
import com.ww.model.entity.outside.wisie.ProfileWisie;
import com.ww.play.PlayCommunication;
import com.ww.play.PlayManager;
import com.ww.play.command.PlayAnswerCommand;
import com.ww.play.command.PlayChooseTaskPropsCommand;
import com.ww.play.command.PlaySurrenderCommand;
import com.ww.service.rival.war.RivalWarService;
import com.ww.websocket.message.Message;

import java.util.List;

import static com.ww.service.rival.global.RivalMessageService.*;

public class PlayWarManager extends PlayManager {
    public PlayWarManager(RivalTwoPlayerInit initContainer, RivalWarService rivalService) {
        super(initContainer, rivalService);
        this.model = new PlayWarModel(prepareTeams());
        this.communication = new PlayCommunication(model);
        this.flow = new PlayWarFlow(this, model, new WarInterval());
        initCommandMap();
    }

    protected WarTeams prepareTeams() {
        WarTeams teams = new WarTeams();
        Profile creatorProfile = ((RivalTwoPlayerInit) initContainer).getCreatorProfile();
        teams.addProfile(creatorProfile.getId(), prepareTeam(creatorProfile));
        Profile opponentProfile = ((RivalTwoPlayerInit) initContainer).getOpponentProfile();
        teams.addProfile(opponentProfile.getId(), prepareTeam(opponentProfile));
        return teams;
    }

    protected WarTeam prepareTeam(Profile profile) {
        List<ProfileWisie> wisies = ((RivalWarService) rivalService).getProfileWisies(profile);
        List<TeamMember> teamMembers = TeamHelper.prepareTeamMembers(profile, wisies);
        return new WarTeam(profile, teamMembers, new WarTeamSkills(1, teamMembers));
    }

    protected void initCommandMap() {
        commandMap.put(SURRENDER, new PlaySurrenderCommand(model, flow));
        commandMap.put(ANSWER, new PlayAnswerCommand(model, flow));
        commandMap.put(CHOOSE_TASK_PROPS, new PlayChooseTaskPropsCommand(model, flow));
    }

    @Override
    public Message getMessageContent() {
        return Message.WAR_CONTENT;
    }
}
