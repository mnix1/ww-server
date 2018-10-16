package com.ww.manager.rival.challenge;

import com.ww.helper.TeamHelper;
import com.ww.manager.rival.war.WarManager;
import com.ww.model.constant.Category;
import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.model.container.rival.campaign.CampaignWarModel;
import com.ww.model.container.rival.challenge.ChallengeFlow;
import com.ww.model.container.rival.challenge.ChallengeInterval;
import com.ww.model.container.rival.init.RivalChallengeInit;
import com.ww.model.container.rival.war.TeamMember;
import com.ww.model.container.rival.war.WarModelFactory;
import com.ww.model.container.rival.war.WarTeam;
import com.ww.model.container.rival.war.WarTeams;
import com.ww.model.container.rival.war.skill.EmptyTeamSkills;
import com.ww.model.container.rival.war.skill.WarTeamSkills;
import com.ww.model.dto.rival.task.TaskDTO;
import com.ww.model.entity.outside.rival.challenge.ChallengePhase;
import com.ww.model.entity.outside.rival.challenge.ChallengeProfile;
import com.ww.model.entity.outside.rival.task.Question;
import com.ww.model.entity.outside.social.Profile;
import com.ww.model.entity.outside.wisie.ProfileWisie;
import com.ww.service.rival.challenge.RivalChallengeService;
import com.ww.websocket.message.Message;

import java.util.Arrays;
import java.util.List;

import static com.ww.helper.TeamHelper.isBotProfile;

public class ChallengeManager extends WarManager {

    public ChallengeProfile challengeProfile;
    public List<ChallengePhase> challengePhases;

    public ChallengeManager(RivalChallengeInit init, RivalChallengeService rivalService) {
        this.rivalService = rivalService;
        this.challengeProfile = init.getChallengeProfile();
        this.challengePhases = init.getChallengePhases();
        WarTeams teams = new WarTeams();
        this.model = new CampaignWarModel(init, teams);
        this.modelFactory = new WarModelFactory(this.model);
        this.interval = new ChallengeInterval(this.model);
        this.flow = new ChallengeFlow(this);

        Profile creator = init.getCreatorProfile();
        List<ProfileWisie> creatorWisies = rivalService.getProfileWisies(creator);
        List<TeamMember> teamMembers = TeamHelper.prepareTeamMembers(creator, creatorWisies);
        WarTeam creatorTeam = new WarTeam(creator, teamMembers, new WarTeamSkills(1, teamMembers));
        teams.addProfile(creator.getId(), creatorTeam);

        Profile opponent = init.getOpponentProfile();
        WarTeam opponentTeam = new WarTeam(opponent, TeamHelper.prepareTeamMembers(Arrays.asList(challengePhases.get(0).getPhaseWisie())), new EmptyTeamSkills());
        teams.addProfile(opponent.getId(), opponentTeam);
    }

    @Override
    public boolean isEnd() {
        for (WarTeam team : getModel().getTeams().getTeams()) {
            if (!isBotProfile(team.getProfile()) && !team.isAnyPresentMember()) {
                return true;
            }
        }
        return false;
    }

    public ChallengePhase findPhase(Long id, Category category, DifficultyLevel difficultyLevel) {
        RivalChallengeService rivalChallengeService = (RivalChallengeService) rivalService;
        int taskIndex = id.intValue() - 1;
        if (challengePhases.size() <= taskIndex) {
            challengePhases.add(rivalChallengeService.preparePhase(challengeProfile.getChallenge(), taskIndex, category, difficultyLevel));
        }
        return challengePhases.get(taskIndex);
    }

    public ChallengePhase currentPhase() {
        return challengePhases.get(model.getCurrentTaskIndex());
    }

    @Override
    public void prepareTask(Long id, Category category, DifficultyLevel difficultyLevel) {
        RivalChallengeService rivalChallengeService = (RivalChallengeService) rivalService;
        ChallengePhase phase = findPhase(id, category, difficultyLevel);
        Question question = rivalChallengeService.getTaskGenerateService().generate(phase.getTaskType(), phase.getDifficultyLevel(), phase.getLanguage());
        question.setId(id);
        question.initAnswerIds();
        rivalChallengeService.initTaskWisdomAttributes(question);
        TaskDTO taskDTO = rivalService.prepareTaskDTO(question);
        model.addTask(question, taskDTO);
    }

    @Override
    public Message getMessageContent() {
        return Message.CHALLENGE_CONTENT;
    }
}
