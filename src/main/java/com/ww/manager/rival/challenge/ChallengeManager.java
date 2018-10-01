package com.ww.manager.rival.challenge;

import com.ww.manager.rival.war.WarManager;
import com.ww.model.constant.Category;
import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.model.container.rival.init.RivalChallengeInitContainer;
import com.ww.model.container.rival.init.RivalInitContainer;
import com.ww.model.container.rival.init.RivalOnePlayerInitContainer;
import com.ww.model.container.rival.war.WarContainer;
import com.ww.model.container.rival.war.WarModelFactory;
import com.ww.model.container.rival.war.WarProfileContainer;
import com.ww.model.dto.rival.task.AnswerDTO;
import com.ww.model.dto.rival.task.TaskDTO;
import com.ww.model.entity.outside.rival.challenge.ChallengeProfile;
import com.ww.model.entity.outside.rival.challenge.ChallengeQuestion;
import com.ww.model.entity.outside.rival.task.Question;
import com.ww.model.entity.outside.social.Profile;
import com.ww.model.entity.outside.wisie.ProfileWisie;
import com.ww.service.rival.challenge.RivalChallengeService;
import com.ww.service.social.ProfileConnectionService;

import java.util.Comparator;
import java.util.List;

public class ChallengeManager extends WarManager {

    public ChallengeProfile challengeProfile;
    public List<ChallengeQuestion> challengeQuestions;

    public ChallengeManager(RivalChallengeInitContainer container, RivalChallengeService rivalChallengeService, ProfileConnectionService profileConnectionService) {
        this.abstractRivalService = rivalChallengeService;
        this.profileConnectionService = profileConnectionService;
        Profile creator = container.getCreatorProfile();
        Long creatorId = creator.getId();
        List<ProfileWisie> creatorWisies = rivalChallengeService.getProfileWisies(creator);
        this.rivalContainer = new WarContainer();
        this.rivalContainer.storeInformationFromInitContainer(container);
        this.rivalContainer.addProfile(creatorId, new WarProfileContainer(creator, prepareTeamMembers(creator, creatorWisies)));
        this.warContainer = (WarContainer) this.rivalContainer;
        this.challengeProfile = container.getChallengeProfile();
        this.challengeQuestions = container.getChallengeQuestions();
        this.modelFactory = new WarModelFactory(warContainer);
    }

    @Override
    public void prepareTask(Long id, Category category, DifficultyLevel difficultyLevel) {
        RivalChallengeService rivalChallengeService = (RivalChallengeService) abstractRivalService;
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
        TaskDTO taskDTO = abstractRivalService.prepareTaskDTO(question);
        taskDTO.getAnswers().sort(Comparator.comparing(AnswerDTO::getId));
        rivalContainer.addTask(question, taskDTO);
    }

    @Override
    public Integer getAnsweringInterval() {
        int taskIndex = Math.max(0, rivalContainer.getCurrentTaskIndex());
        Integer interval = super.getAnsweringInterval() - taskIndex * 5000;
        return Math.max(interval, 5000);
    }

}
