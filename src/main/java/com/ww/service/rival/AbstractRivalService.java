package com.ww.service.rival;

import com.ww.manager.rival.RivalManager;
import com.ww.model.constant.Category;
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
        for (RivalTeam profileContainer : manager.getModel().getTeamsContainer().getTeams()) {
            getRivalGlobalService().remove(profileContainer.getProfileId());
        }
        RivalModel rivalModel = manager.getModel();
        Boolean isDraw = rivalModel.getDraw();
        Profile winner = rivalModel.getWinner();
        Rival rival = new Rival(rivalModel.getType(), rivalModel.getImportance(), rivalModel.getCreatorProfile(), rivalModel.getOpponentProfile(), isDraw, winner);
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
