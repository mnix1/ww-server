package com.ww.service;

import com.ww.manager.rival.RivalManager;
import com.ww.model.constant.Category;
import com.ww.model.constant.Language;
import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.model.container.rival.RivalModel;
import com.ww.model.container.rival.RivalTeam;
import com.ww.model.dto.rival.task.TaskDTO;
import com.ww.model.entity.outside.rival.Rival;
import com.ww.model.entity.outside.rival.task.Question;
import com.ww.model.entity.outside.social.Profile;
import com.ww.service.rival.global.RivalGlobalService;
import com.ww.service.rival.task.TaskGenerateService;
import com.ww.service.rival.task.TaskRendererService;
import com.ww.service.social.ProfileConnectionService;
import com.ww.service.social.ProfileService;
import com.ww.service.social.RewardService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Getter
public class RivalService {
    @Autowired
    private ProfileConnectionService profileConnectionService;

    @Autowired
    private TaskGenerateService taskGenerateService;

    @Autowired
    private TaskRendererService taskRendererService;

    @Autowired
    private RewardService rewardService;

    @Autowired
    private ProfileService profileService;

    @Autowired
    private RivalGlobalService rivalGlobalService;

    public void addRewardFromWin(Profile winner) {
    }

    public void disposeManager(RivalManager manager) {
        if (!manager.isClosed()) {
            return;
        }
        for (RivalTeam profileContainer : manager.getModel().getTeams().getTeams()) {
            rivalGlobalService.remove(profileContainer.getProfileId());
        }
        RivalModel rivalModel = manager.getModel();
        Boolean isDraw = rivalModel.getDraw();
        Profile winner = rivalModel.getWinner();
        Rival rival = new Rival(rivalModel.getType(), rivalModel.getImportance(), rivalModel.getCreatorProfile(), rivalModel.getOpponentProfile(), isDraw, winner);
        if (!isDraw) {
            addRewardFromWin(winner);
        }
        rivalGlobalService.save(rival);
        // TODO STORE RESULT
    }

    public Question prepareQuestion(Category category, DifficultyLevel difficultyLevel, Language language) {
        return taskGenerateService.generate(category, difficultyLevel, language);
    }

    public TaskDTO prepareTaskDTO(Question question) {
        return taskRendererService.prepareTaskDTO(question);
    }

}
