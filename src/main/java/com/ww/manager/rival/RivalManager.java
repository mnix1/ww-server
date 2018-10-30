package com.ww.manager.rival;

import com.ww.helper.Describe;
import com.ww.model.constant.Category;
import com.ww.model.constant.Language;
import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.container.rival.RivalInterval;
import com.ww.model.container.rival.RivalModel;
import com.ww.model.container.rival.RivalModelFactory;
import com.ww.model.container.rival.RivalTeam;
import com.ww.model.dto.rival.task.TaskDTO;
import com.ww.model.entity.outside.rival.task.Question;
import com.ww.service.RivalService;
import com.ww.websocket.message.Message;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

@Getter
public abstract class RivalManager implements Describe {
    protected static final Logger logger = LoggerFactory.getLogger(RivalManager.class);

    protected RivalService rivalService;

    public abstract RivalModelFactory getModelFactory();

    public abstract RivalModel getModel();

    public abstract RivalInterval getInterval();

    public abstract RivalFlow getFlow();

    public abstract boolean isEnd();

    public abstract Message getMessageContent();

    public RivalTeam getTeam(Long profileId) {
        return getModel().getTeams().team(profileId);
    }

    public void prepareTask(Long id) {
        prepareTask(id, Category.random(), DifficultyLevel.random());
    }

    protected Language findQuestionLanguage() {
        Language creatorLanguage = getModel().getCreatorProfile().getLanguage();
        Language opponentLanguage = getModel().getOpponentProfile() == null ? Language.NONE : getModel().getOpponentProfile().getLanguage();
        if (creatorLanguage == opponentLanguage) {
            return creatorLanguage;
        }
        if (opponentLanguage == Language.NONE) {
            return creatorLanguage;
        }
        return Language.NO_COMMON;
    }

    public void prepareTask(Long id, Category category, DifficultyLevel difficultyLevel) {
        Question question = rivalService.prepareQuestion(category, difficultyLevel, findQuestionLanguage());
        question.setId(id);
        question.initAnswerIds();
        TaskDTO taskDTO = rivalService.prepareTaskDTO(question);
        getModel().addTask(question, taskDTO);
    }

    public synchronized Map<String, Object> actualModel(Long profileId) {
        Map<String, Object> model = new HashMap<>();
        RivalTeam profileContainer = getModel().getTeams().team(profileId);
        getModelFactory().fillModel(model, profileContainer);
        return model;
    }

    public void send(Map<String, Object> model, Message message, Long profileId) {
        rivalService.getProfileConnectionService().send(profileId, model, message);
    }

    public boolean isClosed() {
        return getModel().getStatus() == RivalStatus.CLOSED;
    }

    @Override
    public String describe() {
        return "class=" + this.getClass().getName()
                + ", status=" + getModel().getStatus()
                + ", creatorId=" + getModel().getCreatorProfile().getId()
                + (getModel().getOpponentProfile() == null ? "" : ", opponentId=" + getModel().getOpponentProfile().getId());

    }

}
