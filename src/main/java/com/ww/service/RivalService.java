package com.ww.service;

import com.ww.game.play.PlayManager;
import com.ww.game.play.container.PlayContainer;
import com.ww.model.constant.Category;
import com.ww.model.constant.Language;
import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.model.constant.rival.RivalImportance;
import com.ww.model.container.rival.RivalTeam;
import com.ww.model.dto.rival.task.TaskDTO;
import com.ww.model.entity.outside.rival.task.Question;
import com.ww.model.entity.outside.social.Profile;
import com.ww.service.rival.global.RivalGlobalService;
import com.ww.service.rival.season.RivalProfileSeasonService;
import com.ww.service.rival.season.RivalSeasonService;
import com.ww.service.rival.task.TaskGenerateService;
import com.ww.service.rival.task.TaskRendererService;
import com.ww.service.social.ConnectionService;
import com.ww.service.social.ProfileService;
import com.ww.service.social.RewardService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@Getter
@AllArgsConstructor
public class RivalService {
    private static Logger logger = LoggerFactory.getLogger(RivalService.class);

    private final ConnectionService connectionService;
    private final TaskGenerateService taskGenerateService;
    private final TaskRendererService taskRendererService;
    private final RewardService rewardService;
    private final RivalSeasonService rivalSeasonService;
    private final ProfileService profileService;
    private final RivalGlobalService rivalGlobalService;
    private final RivalProfileSeasonService rivalProfileSeasonService;

    public void addRewardFromWin(Profile winner) {
    }

    public void disposeManager(PlayManager manager) {
        for (RivalTeam profileContainer : manager.getContainer().getTeams().getTeams()) {
            rivalGlobalService.remove(profileContainer.getProfileId());
        }
        PlayContainer container = manager.getContainer();
        if (container.getResult().getDraw() != null) {
            if (!container.getResult().getDraw() && container.getInit().getImportance() != RivalImportance.FRIEND) {
                addRewardFromWin(container.getResult().getWinner());
            }
            updateSeason(manager);
        }
        manager.getRival().update(manager);
        rivalGlobalService.save(manager.getRival());
        logger.debug("rival disposeManager {}", manager.getRival().toString());
    }

    public void updateSeason(PlayManager manager) {
        if (!manager.getContainer().isRanking()) {
            return;
        }
        rivalProfileSeasonService.update(manager.getContainer().getInit().getSeason());
    }

    public void updateProfilesElo(PlayContainer container) {
        if (!container.isRanking()) {
            return;
        }
        rivalProfileSeasonService.updateProfilesElo(container);
    }

    public Question prepareQuestion(Category category, DifficultyLevel difficultyLevel, Language language) {
        return taskGenerateService.generate(category, difficultyLevel, language);
    }

    public TaskDTO prepareTaskDTO(Question question) {
        return taskRendererService.prepareTaskDTO(question);
    }

}
