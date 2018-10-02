package com.ww.service.rival;

import com.ww.manager.rival.RivalManager;
import com.ww.model.constant.Category;
import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.model.container.rival.RivalContainer;
import com.ww.model.container.rival.RivalTeamContainer;
import com.ww.model.dto.rival.task.TaskDTO;
import com.ww.model.entity.outside.rival.Rival;
import com.ww.model.entity.outside.rival.task.Question;
import com.ww.model.entity.outside.social.Profile;
import com.ww.service.rival.global.RivalGlobalService;
import com.ww.service.rival.task.TaskGenerateService;
import com.ww.service.rival.task.TaskRendererService;
import com.ww.service.social.ProfileConnectionService;
import com.ww.service.social.ProfileService;
import com.ww.websocket.message.Message;

public abstract class AbstractRivalService {
    protected abstract void addRewardFromWin(Profile winner);

    public abstract Message getMessageContent();

    protected abstract ProfileConnectionService getProfileConnectionService();

    protected abstract TaskGenerateService getTaskGenerateService();

    protected abstract TaskRendererService getTaskRendererService();

    public abstract ProfileService getProfileService();

    public abstract RivalGlobalService getRivalGlobalService();

    public synchronized void disposeManager(RivalManager manager) {
        if (!manager.isClosed()) {
            return;
        }
        for (RivalTeamContainer profileContainer : manager.getContainer().getTeamsContainer().getTeamContainers()) {
            getRivalGlobalService().remove(profileContainer.getProfileId());
        }
        RivalContainer rivalContainer = manager.getContainer();
        Boolean isDraw = rivalContainer.getDraw();
        Profile winner = rivalContainer.getWinner();
        Rival rival = new Rival(rivalContainer.getType(), rivalContainer.getImportance(), rivalContainer.getCreatorProfile(), rivalContainer.getOpponentProfile(), isDraw, winner);
        if (!isDraw) {
            addRewardFromWin(winner);
        }
        getRivalGlobalService().save(rival);
        // TODO STORE RESULT
    }

    public Question prepareQuestion(Category category, DifficultyLevel difficultyLevel) {
        return getTaskGenerateService().generate(category, difficultyLevel);
    }

    public TaskDTO prepareTaskDTO(Question question) {
        return getTaskRendererService().prepareTaskDTO(question);
    }

}
