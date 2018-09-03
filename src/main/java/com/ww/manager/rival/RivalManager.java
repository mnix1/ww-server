package com.ww.manager.rival;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ww.manager.rival.state.StateSurrender;
import com.ww.model.constant.Category;
import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.constant.rival.RivalType;
import com.ww.model.constant.rival.task.TaskDifficultyLevel;
import com.ww.model.container.rival.RivalContainer;
import com.ww.model.container.rival.RivalProfileContainer;
import com.ww.model.dto.rival.task.TaskDTO;
import com.ww.model.entity.rival.task.Question;
import com.ww.model.entity.social.Profile;
import com.ww.service.rival.RivalService;
import com.ww.service.social.ProfileConnectionService;
import com.ww.websocket.message.Message;
import com.ww.websocket.message.MessageDTO;
import io.reactivex.disposables.Disposable;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ww.helper.EloHelper.*;

@Getter
public abstract class RivalManager {
    protected static final Logger logger = LoggerFactory.getLogger(RivalManager.class);

    protected RivalContainer rivalContainer;
    protected RivalService rivalService;
    protected ProfileConnectionService profileConnectionService;
    protected Disposable answeringTimeoutDisposable;
    protected Disposable choosingTaskPropsDisposable;

    public void disposeFlowable() {
        if (answeringTimeoutDisposable != null) {
            answeringTimeoutDisposable.dispose();
            answeringTimeoutDisposable = null;
        }
        if (choosingTaskPropsDisposable != null) {
            choosingTaskPropsDisposable.dispose();
            choosingTaskPropsDisposable = null;
        }
    }

    protected abstract Message getMessageReadyFast();

    public abstract Message getMessageContent();

    public void prepareTask(Long id) {
        prepareTask(id, Category.random(), TaskDifficultyLevel.random());
    }

    public void prepareTask(Long id, Category category, TaskDifficultyLevel difficultyLevel) {
        Question question = rivalService.prepareQuestion(category, difficultyLevel);
        question.setId(id);
        TaskDTO taskDTO = rivalService.prepareTaskDTO(question);
        rivalContainer.addTask(question, taskDTO);
    }

    public synchronized void maybeStart(Long profileId) {
        rivalContainer.profileReady(profileId);
        if (rivalContainer.isReady() && rivalContainer.getStatus() == RivalStatus.OPEN) {
            start();
        }
    }

    public void updateProfilesElo() {
        if (!rivalContainer.isRanking()) {
            return;
        }
        Profile winner = rivalContainer.getWinner();
        Profile creator = rivalContainer.getCreatorProfile();
        Profile opponent = rivalContainer.getOpponentProfile();
        Long creatorEloChange = 0L;
        Long opponentEloChange = 0L;
        if (rivalContainer.getType() == RivalType.BATTLE) {
            if (rivalContainer.getDraw()) {
                creatorEloChange = prepareEloChange(creator.getBattleElo(), opponent.getBattleElo(), DRAW);
                opponentEloChange = prepareEloChange(opponent.getBattleElo(), creator.getBattleElo(), DRAW);
            } else {
                creatorEloChange = prepareEloChange(creator.getBattleElo(), opponent.getBattleElo(), creator.equals(winner) ? WINNER : LOOSER);
                opponentEloChange = prepareEloChange(opponent.getBattleElo(), creator.getBattleElo(), opponent.equals(winner) ? WINNER : LOOSER);
            }
        } else if (rivalContainer.getType() == RivalType.WAR) {
            if (rivalContainer.getDraw()) {
                creatorEloChange = prepareEloChange(creator.getWarElo(), opponent.getWarElo(), DRAW);
                opponentEloChange = prepareEloChange(opponent.getWarElo(), creator.getWarElo(), DRAW);
            } else {
                creatorEloChange = prepareEloChange(creator.getWarElo(), opponent.getWarElo(), creator.equals(winner) ? WINNER : LOOSER);
                opponentEloChange = prepareEloChange(opponent.getWarElo(), creator.getWarElo(), opponent.equals(winner) ? WINNER : LOOSER);
            }
        }
        updateElo(creator, creatorEloChange, rivalContainer.getType());
        rivalContainer.setCreatorEloChange(creatorEloChange);
        updateElo(opponent, opponentEloChange, rivalContainer.getType());
        rivalContainer.setOpponentEloChange(opponentEloChange);
        rivalService.getProfileService().save(creator);
        rivalService.getProfileService().save(opponent);
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
        RivalProfileContainer rivalProfileContainer = rivalContainer.getProfileIdRivalProfileContainerMap().get(profileId);
        rivalContainer.fillModel(model, rivalProfileContainer);
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

    public void sendReadyFast() {
        rivalContainer.getProfileIdRivalProfileContainerMap().values().stream().forEach(rivalProfileContainer -> {
            Map<String, Object> model = new HashMap<>();
            send(model, getMessageReadyFast(), rivalProfileContainer.getProfileId());
        });
    }


    public Integer getIntroInterval() {
        return 8500;
    }

    public Integer getPreparingNextTaskInterval() {
        return 2000;
    }

    public Integer getAnsweringInterval() {
        return 45000;
    }

    public Integer getAnsweringTimeoutInterval() {
        return 8000;
    }

    public Integer getShowingAnswerInterval() {
        return 8000;
//        return 800000;
    }

    public Integer getChoosingTaskPropsInterval() {
        return 14000;
//        return 10004000;
    }

    public Integer getRandomChooseTaskPropsInterval() {
        return 6000;
    }

    public boolean canAnswer() {
        return rivalContainer.getStatus() == RivalStatus.ANSWERING;
    }

    public boolean canChooseTaskProps() {
        return rivalContainer.getStatus() == RivalStatus.CHOOSING_TASK_PROPS;
    }

    public boolean canChooseWhoAnswer() {
        return rivalContainer.getStatus() == RivalStatus.CHOOSING_WHO_ANSWER;
    }

    public boolean isClosed() {
        return rivalContainer.getStatus() == RivalStatus.CLOSED;
    }

    public List<RivalProfileContainer> getRivalProfileContainers() {
        return new ArrayList<>(this.rivalContainer.getProfileIdRivalProfileContainerMap().values());
    }

}
