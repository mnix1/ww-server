package com.ww.game.play;

import com.ww.helper.TeamHelper;
import com.ww.model.constant.Category;
import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.model.container.rival.RivalChallengeTasks;
import com.ww.model.container.rival.RivalTasks;
import com.ww.model.container.rival.RivalTeams;
import com.ww.model.container.rival.challenge.ChallengeInterval;
import com.ww.model.container.rival.challenge.ChallengeTeam;
import com.ww.model.container.rival.init.RivalChallengeInit;
import com.ww.model.container.rival.war.TeamMember;
import com.ww.model.container.rival.war.skill.EmptyTeamSkills;
import com.ww.model.container.rival.war.skill.WarTeamSkills;
import com.ww.model.entity.outside.rival.challenge.ChallengePhase;
import com.ww.model.entity.outside.rival.challenge.ChallengeProfile;
import com.ww.model.entity.outside.social.Profile;
import com.ww.model.entity.outside.wisie.ProfileWisie;
import com.ww.game.play.communication.PlayWarCommunication;
import com.ww.game.play.container.PlayChallengeContainer;
import com.ww.game.play.flow.PlayChallengeFlow;
import com.ww.game.play.flow.PlayWarFlow;
import com.ww.service.rival.challenge.RivalChallengeService;
import com.ww.service.rival.war.RivalWarService;
import com.ww.websocket.message.Message;
import lombok.Getter;

import java.util.Collections;
import java.util.List;

@Getter
public class PlayChallengeManager extends PlayWarManager {
    public PlayChallengeManager(RivalChallengeInit init, RivalChallengeService rivalService) {
        super(init, rivalService);
        this.container = new PlayChallengeContainer(init, prepareTeams(init), prepareTasks(init), prepareTimeouts(), prepareDecisions(), prepareResult());
        this.flow = new PlayChallengeFlow(this, new ChallengeInterval(container));
        this.communication = new PlayWarCommunication(this, Message.CHALLENGE_CONTENT);
    }

    protected RivalTeams prepareTeams(RivalChallengeInit init) {
        ChallengeTeam opponentTeam = new ChallengeTeam(init.getOpponentProfile(),
                TeamHelper.prepareTeamMembers(Collections.singletonList(init.getChallengePhases().get(0).getPhaseWisie())),
                new EmptyTeamSkills());
        return new RivalTeams(prepareTeam(init.getCreatorProfile()), opponentTeam);
    }

    protected ChallengeTeam prepareTeam(Profile profile) {
        List<ProfileWisie> wisies = ((RivalWarService) service).getProfileWisies(profile);
        List<TeamMember> teamMembers = TeamHelper.prepareTeamMembers(profile, wisies);
        return new ChallengeTeam(profile, teamMembers, new WarTeamSkills(1, teamMembers));
    }

    protected RivalTasks prepareTasks(RivalChallengeInit init) {
        return new RivalChallengeTasks((RivalChallengeService) service, init.getChallengeProfile(), init.getChallengePhases());
    }
}
