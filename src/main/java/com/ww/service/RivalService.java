package com.ww.service;

import com.ww.game.play.PlayManager;
import com.ww.game.play.container.PlayContainer;
import com.ww.model.constant.Category;
import com.ww.model.constant.Language;
import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.model.container.rival.RivalTeam;
import com.ww.model.dto.rival.task.TaskDTO;
import com.ww.model.entity.outside.rival.season.ProfileSeason;
import com.ww.model.entity.outside.rival.task.Question;
import com.ww.model.entity.outside.social.Profile;
import com.ww.service.rival.global.RivalGlobalService;
import com.ww.service.rival.season.RivalProfileSeasonService;
import com.ww.service.rival.task.TaskGenerateService;
import com.ww.service.rival.task.TaskRendererService;
import com.ww.service.social.ConnectionService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Getter
@AllArgsConstructor
public class RivalService {
    private static Logger logger = LoggerFactory.getLogger(RivalService.class);

    private ConnectionService connectionService;
    private TaskGenerateService taskGenerateService;
    private TaskRendererService taskRendererService;
    private RivalGlobalService rivalGlobalService;
    private RivalProfileSeasonService rivalProfileSeasonService;

    @Transactional
    public void disposeManager(PlayManager manager) {
        for (RivalTeam profileContainer : manager.getContainer().getTeams().getTeams()) {
            rivalGlobalService.remove(profileContainer.getProfileId());
        }
        PlayContainer container = manager.getContainer();
        addReward(container);
        updateSeason(container);
        addExperience(container);
        manager.getRival().update(manager);
        rivalGlobalService.save(manager.getRival());
        logger.debug("rival disposeManager {}", manager.getRival().toString());
    }

    public void addReward(PlayContainer container) {
        if (container.isDraw() || container.isFriend() || container.isTraining()) {
            return;
        }
        Profile winner = container.getResult().getWinner();
        addReward(winner, container.isRanking() ? container.getInit().getProfileSeason(winner.getId()) : null);
    }

    public void addReward(Profile winner, ProfileSeason winnerSeason) {
    }

    public void addExperience(PlayContainer container) {
    }

    public void updateSeason(PlayContainer container) {
        if (!container.isRanking()) {
            return;
        }
        rivalProfileSeasonService.update(container.getInit().getSeason());
    }

    public void updateProfilesElo(PlayContainer container) {
        rivalProfileSeasonService.updateProfilesElo(container);
    }

    public Question prepareQuestion(Category category, DifficultyLevel difficultyLevel, Language language) {
        return taskGenerateService.generate(category, difficultyLevel, language);
    }

    public TaskDTO prepareTaskDTO(Question question) {
        return taskRendererService.prepareTaskDTO(question);
    }
}
