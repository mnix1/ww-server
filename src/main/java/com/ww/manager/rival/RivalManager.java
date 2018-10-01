package com.ww.manager.rival;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ww.manager.rival.state.StateSurrender;
import com.ww.model.constant.Category;
import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.constant.rival.RivalType;
import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.model.container.rival.RivalContainer;
import com.ww.model.container.rival.RivalInterval;
import com.ww.model.container.rival.RivalModelFactory;
import com.ww.model.container.rival.RivalProfileContainer;
import com.ww.model.dto.rival.task.TaskDTO;
import com.ww.model.entity.outside.rival.task.Question;
import com.ww.model.entity.outside.social.Profile;
import com.ww.service.rival.AbstractRivalService;
import com.ww.service.social.ProfileConnectionService;
import com.ww.websocket.message.Message;
import com.ww.websocket.message.MessageDTO;
import io.reactivex.disposables.Disposable;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ww.helper.EloHelper.*;
import static com.ww.service.rival.global.RivalMessageService.*;

@Getter
public abstract class RivalManager {
    protected static final Logger logger = LoggerFactory.getLogger(RivalManager.class);

    protected AbstractRivalService abstractRivalService;
    protected ProfileConnectionService profileConnectionService;
    protected Disposable activeFlowable;

    public abstract RivalModelFactory getModelFactory();
    public abstract RivalContainer getContainer();
    public abstract RivalInterval getInterval();

    public void disposeFlowable() {
        if (activeFlowable != null) {
            activeFlowable.dispose();
            activeFlowable = null;
        }
    }

    public boolean processMessage(Long profileId, Map<String, Object> content) {
        String id = (String) content.get("id");
        if (id.equals(ANSWER)) {
            answer(profileId, content);
            return true;
        } else if (id.equals(CHOOSE_TASK_PROPS)) {
            chosenTaskProps(profileId, content);
            return true;
        } else if (id.equals(SURRENDER)) {
            surrender(profileId);
            return true;
        } else {
            return false;
        }
    }

    public Message getMessageContent() {
        return abstractRivalService.getMessageContent();
    }

    public void prepareTask(Long id) {
        prepareTask(id, Category.random(), DifficultyLevel.random());
    }

    public void prepareTask(Long id, Category category, DifficultyLevel difficultyLevel) {
        Question question = abstractRivalService.prepareQuestion(category, difficultyLevel);
        question.setId(id);
        question.initAnswerIds();
        TaskDTO taskDTO = abstractRivalService.prepareTaskDTO(question);
        getContainer().addTask(question, taskDTO);
    }

    public synchronized void updateProfilesElo() {
        if (!getContainer().isRanking()) {
            return;
        }
        Profile winner = getContainer().getWinner();
        Profile creator = getContainer().getCreatorProfile();
        Profile opponent = getContainer().getOpponentProfile();
        Instant lastPlay = Instant.now();
        Long creatorEloChange = 0L;
        Long opponentEloChange = 0L;
        if (getContainer().getType() == RivalType.BATTLE) {
            if (getContainer().getDraw()) {
                creatorEloChange = prepareEloChange(creator.getBattleElo(), opponent.getBattleElo(), DRAW);
                opponentEloChange = prepareEloChange(opponent.getBattleElo(), creator.getBattleElo(), DRAW);
            } else {
                creatorEloChange = prepareEloChange(creator.getBattleElo(), opponent.getBattleElo(), creator.equals(winner) ? WINNER : LOOSER);
                opponentEloChange = prepareEloChange(opponent.getBattleElo(), creator.getBattleElo(), opponent.equals(winner) ? WINNER : LOOSER);
            }
            creator.setBattleLastPlay(lastPlay);
            opponent.setBattleLastPlay(lastPlay);
        } else if (getContainer().getType() == RivalType.WAR) {
            if (getContainer().getDraw()) {
                creatorEloChange = prepareEloChange(creator.getWarElo(), opponent.getWarElo(), DRAW);
                opponentEloChange = prepareEloChange(opponent.getWarElo(), creator.getWarElo(), DRAW);
            } else {
                creatorEloChange = prepareEloChange(creator.getWarElo(), opponent.getWarElo(), creator.equals(winner) ? WINNER : LOOSER);
                opponentEloChange = prepareEloChange(opponent.getWarElo(), creator.getWarElo(), opponent.equals(winner) ? WINNER : LOOSER);
            }
            creator.setWarLastPlay(lastPlay);
            opponent.setWarLastPlay(lastPlay);
        }
        updateElo(creator, creatorEloChange, getContainer().getType());
        getContainer().setCreatorEloChange(creatorEloChange);
        updateElo(opponent, opponentEloChange, getContainer().getType());
        getContainer().setOpponentEloChange(opponentEloChange);
        abstractRivalService.getProfileService().save(creator);
        abstractRivalService.getProfileService().save(opponent);
    }

    public abstract boolean isEnd();

    public abstract void start();

    public abstract void answer(Long profileId, Map<String, Object> content);

    public abstract void chosenTaskProps(Long profileId, Map<String, Object> content);

    public synchronized void surrender(Long profileId) {
        new StateSurrender(this, profileId).startVoid();
    }

    public synchronized Map<String, Object> actualModel(Long profileId) {
        Map<String, Object> model = new HashMap<>();
        RivalProfileContainer rivalProfileContainer = getContainer().getProfileContainers().get(profileId);
        getModelFactory().fillModel(model, rivalProfileContainer);
        return model;
    }

    public void send(Map<String, Object> model, Message message, Long profileId) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            profileConnectionService.sendMessage(profileId, new MessageDTO(message, objectMapper.writeValueAsString(model)).toString());
        } catch (JsonProcessingException e) {
            logger.error("Error when sending message");
        }
    }

    public boolean canAnswer() {
        return getContainer().getStatus() == RivalStatus.ANSWERING;
    }

    public boolean canChooseTaskProps() {
        return getContainer().getStatus() == RivalStatus.CHOOSING_TASK_PROPS;
    }

    public boolean canChooseWhoAnswer() {
        return getContainer().getStatus() == RivalStatus.CHOOSING_WHO_ANSWER;
    }

    public boolean isClosed() {
        return getContainer().getStatus() == RivalStatus.CLOSED;
    }

    public List<RivalProfileContainer> getRivalProfileContainers() {
        return new ArrayList<>(this.getContainer().getProfileContainers().values());
    }

}
