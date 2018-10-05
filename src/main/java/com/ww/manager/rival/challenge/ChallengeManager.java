package com.ww.manager.rival.challenge;

import com.ww.manager.rival.war.WarFlow;
import com.ww.manager.rival.war.WarManager;
import com.ww.model.constant.Category;
import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.model.container.rival.challenge.ChallengeInterval;
import com.ww.model.container.rival.init.RivalChallengeInit;
import com.ww.model.container.rival.war.*;
import com.ww.model.container.rival.war.skill.WarTeamSkills;
import com.ww.model.dto.rival.task.AnswerDTO;
import com.ww.model.dto.rival.task.TaskDTO;
import com.ww.model.entity.outside.rival.challenge.ChallengeProfile;
import com.ww.model.entity.outside.rival.challenge.ChallengeQuestion;
import com.ww.model.entity.outside.rival.task.Question;
import com.ww.model.entity.outside.social.Profile;
import com.ww.model.entity.outside.wisie.ProfileWisie;
import com.ww.service.rival.challenge.RivalChallengeService;
import com.ww.websocket.message.Message;

import java.util.Comparator;
import java.util.List;

public class ChallengeManager extends WarManager {

    public ChallengeProfile challengeProfile;
    public List<ChallengeQuestion> challengeQuestions;

    public ChallengeManager(RivalChallengeInit init, RivalChallengeService rivalService) {
        this.rivalService = rivalService;
        this.challengeProfile = init.getChallengeProfile();
        this.challengeQuestions = init.getChallengeQuestions();
        WarTeams teams = new WarTeams();
        this.model = new WarModel(init, teams);
        this.modelFactory = new WarModelFactory(this.model);
        this.interval = new ChallengeInterval(this.model);
        this.flow = new WarFlow(this);

        Profile creator = init.getCreatorProfile();
        List<ProfileWisie> creatorWisies = rivalService.getProfileWisies(creator);
        WarTeam creatorTeam = new WarTeam(creator, prepareTeamMembers(creator, creatorWisies), new WarTeamSkills(1, creatorWisies));
        teams.addProfile(creator.getId(), creatorTeam);
    }

    @Override
    public void prepareTask(Long id, Category category, DifficultyLevel difficultyLevel) {
        RivalChallengeService rivalChallengeService = (RivalChallengeService) rivalService;
        Question question;
        int taskIndex = id.intValue() - 1;
        if (challengeQuestions.size() > taskIndex) {
            question = challengeQuestions.get(taskIndex).getQuestion();
        } else {
            question = rivalChallengeService.prepareQuestion(challengeProfile, taskIndex, category, difficultyLevel);
        }
        rivalChallengeService.initTaskWisdomAttributes(question);
        question.setId(id);
        question.rewriteAnswerIds();
        TaskDTO taskDTO = rivalService.prepareTaskDTO(question);
        taskDTO.getAnswers().sort(Comparator.comparing(AnswerDTO::getId));
        model.addTask(question, taskDTO);
    }

    @Override
    public Message getMessageContent() {
        return Message.CHALLENGE_CONTENT;
    }
}
