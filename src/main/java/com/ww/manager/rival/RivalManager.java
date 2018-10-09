package com.ww.manager.rival;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ww.model.constant.Category;
import com.ww.model.constant.Language;
import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.constant.rival.RivalType;
import com.ww.model.container.rival.RivalInterval;
import com.ww.model.container.rival.RivalModel;
import com.ww.model.container.rival.RivalModelFactory;
import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.war.WarTeam;
import com.ww.model.dto.rival.task.TaskDTO;
import com.ww.model.entity.outside.rival.task.Question;
import com.ww.model.entity.outside.social.Profile;
import com.ww.service.RivalService;
import com.ww.service.social.ProfileConnectionService;
import com.ww.websocket.message.Message;
import com.ww.websocket.message.MessageDTO;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

import static com.ww.helper.EloHelper.*;

@Getter
public abstract class RivalManager {
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

    public synchronized void updateProfilesElo() {
        if (!getModel().isRanking()) {
            return;
        }
        Profile winner = getModel().getWinner();
        Profile creator = getModel().getCreatorProfile();
        Profile opponent = getModel().getOpponentProfile();
        Instant lastPlay = Instant.now();
        Long creatorEloChange = 0L;
        Long opponentEloChange = 0L;
        if (getModel().getType() == RivalType.BATTLE) {
            if (getModel().getDraw()) {
                creatorEloChange = prepareEloChange(creator.getBattleElo(), opponent.getBattleElo(), DRAW);
                opponentEloChange = prepareEloChange(opponent.getBattleElo(), creator.getBattleElo(), DRAW);
            } else {
                creatorEloChange = prepareEloChange(creator.getBattleElo(), opponent.getBattleElo(), creator.equals(winner) ? WINNER : LOOSER);
                opponentEloChange = prepareEloChange(opponent.getBattleElo(), creator.getBattleElo(), opponent.equals(winner) ? WINNER : LOOSER);
            }
            creator.setBattleLastPlay(lastPlay);
            opponent.setBattleLastPlay(lastPlay);
        } else if (getModel().getType() == RivalType.WAR) {
            if (getModel().getDraw()) {
                creatorEloChange = prepareEloChange(creator.getWarElo(), opponent.getWarElo(), DRAW);
                opponentEloChange = prepareEloChange(opponent.getWarElo(), creator.getWarElo(), DRAW);
            } else {
                creatorEloChange = prepareEloChange(creator.getWarElo(), opponent.getWarElo(), creator.equals(winner) ? WINNER : LOOSER);
                opponentEloChange = prepareEloChange(opponent.getWarElo(), creator.getWarElo(), opponent.equals(winner) ? WINNER : LOOSER);
            }
            creator.setWarLastPlay(lastPlay);
            opponent.setWarLastPlay(lastPlay);
        }
        updateElo(creator, creatorEloChange, getModel().getType());
        getModel().setCreatorEloChange(creatorEloChange);
        updateElo(opponent, opponentEloChange, getModel().getType());
        getModel().setOpponentEloChange(opponentEloChange);
        rivalService.getProfileService().save(creator);
        rivalService.getProfileService().save(opponent);
    }

    public synchronized Map<String, Object> actualModel(Long profileId) {
        Map<String, Object> model = new HashMap<>();
        RivalTeam profileContainer = getModel().getTeams().team(profileId);
        getModelFactory().fillModel(model, profileContainer);
        return model;
    }

    public void send(Map<String, Object> model, Message message, Long profileId) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            rivalService.getProfileConnectionService().sendMessage(profileId, new MessageDTO(message, objectMapper.writeValueAsString(model)).toString());
        } catch (JsonProcessingException e) {
            logger.error("Error when sending message {}", profileId);
        }
    }

    public boolean isClosed() {
        return getModel().getStatus() == RivalStatus.CLOSED;
    }

    @Override
    public String toString() {
        return "RivalManager creatorId=" + getModel().getCreatorProfile().getId()
                + (getModel().getOpponentProfile() == null ? "" : ", opponentId=" + getModel().getOpponentProfile().getId())
                + ", status=" + getModel().getStatus();
    }

}
