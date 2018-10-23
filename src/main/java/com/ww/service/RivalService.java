package com.ww.service;

import com.ww.manager.rival.RivalManager;
import com.ww.model.constant.Category;
import com.ww.model.constant.Language;
import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.model.container.rival.RivalModel;
import com.ww.model.container.rival.RivalTeam;
import com.ww.model.dto.rival.task.TaskDTO;
import com.ww.model.entity.outside.rival.task.Question;
import com.ww.model.entity.outside.social.Profile;
import com.ww.service.rival.global.RivalGlobalService;
import com.ww.service.rival.season.RivalProfileSeasonService;
import com.ww.service.rival.season.RivalSeasonService;
import com.ww.service.rival.task.TaskGenerateService;
import com.ww.service.rival.task.TaskRendererService;
import com.ww.service.social.ProfileConnectionService;
import com.ww.service.social.ProfileService;
import com.ww.service.social.RewardService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;

@Service
@Getter
@AllArgsConstructor
public class RivalService {

    private final ProfileConnectionService profileConnectionService;
    private final TaskGenerateService taskGenerateService;
    private final TaskRendererService taskRendererService;
    private final RewardService rewardService;
    private final RivalSeasonService rivalSeasonService;
    private final ProfileService profileService;
    private final RivalGlobalService rivalGlobalService;
    private final RivalProfileSeasonService rivalProfileSeasonService;

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
        rivalGlobalService.store(rivalModel);
        if (!rivalModel.getDraw()) {
            addRewardFromWin(rivalModel.getWinner());
        }
        updateSeason(manager);
    }

    public void updateSeason(RivalManager manager) {
        if (!manager.getModel().isRanking()) {
            return;
        }
        rivalProfileSeasonService.update(manager.getModel().getSeason());
    }

    public void updateProfilesElo(RivalManager manager) {
        if (!manager.getModel().isRanking()) {
            return;
        }
        rivalProfileSeasonService.updateProfilesElo(manager.getModel());
    }

    public Question prepareQuestion(Category category, DifficultyLevel difficultyLevel, Language language) {
        return taskGenerateService.generate(category, difficultyLevel, language);
    }

    public TaskDTO prepareTaskDTO(Question question) {
        return taskRendererService.prepareTaskDTO(question);
    }

}
