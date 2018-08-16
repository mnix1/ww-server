package com.ww.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ww.model.constant.rival.battle.BattleStatus;
import com.ww.model.container.battle.BattleContainer;
import com.ww.model.container.battle.BattleFriendContainer;
import com.ww.model.container.battle.BattleProfileContainer;
import com.ww.model.dto.rival.task.TaskDTO;
import com.ww.model.entity.rival.task.Question;
import com.ww.service.rival.battle.BattleService;
import com.ww.service.social.ProfileConnectionService;
import com.ww.websocket.message.Message;
import com.ww.websocket.message.MessageDTO;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class BattleManager {
    private static final Logger logger = LoggerFactory.getLogger(BattleManager.class);
    public static final Integer TASK_COUNT = 10;
    public static final Integer ANSWERING_INTERVAL = 600000;
        private static final Integer INTRO_INTERVAL = 20000;
//    private static final Integer INTRO_INTERVAL = 1000;
    private static final Integer NEXT_TASK_INTERVAL = 10000;

    private BattleContainer battleContainer;

    private BattleService battleService;
    private ProfileConnectionService profileConnectionService;

    private Disposable answeringTimeoutDisposable;

    public BattleManager(BattleFriendContainer bic, BattleService battleService, ProfileConnectionService profileConnectionService) {
        Long creatorId = bic.getCreatorProfile().getId();
        Long opponentId = bic.getOpponentProfile().getId();
        this.battleContainer = new BattleContainer();
        this.battleContainer.addProfile(creatorId, new BattleProfileContainer(bic.getCreatorProfile(), opponentId));
        this.battleContainer.addProfile(opponentId, new BattleProfileContainer(bic.getOpponentProfile(), creatorId));
        this.battleService = battleService;
        this.profileConnectionService = profileConnectionService;
    }

    public boolean isLock() {
        return battleContainer.getStatus() != BattleStatus.ANSWERING;
    }

    public boolean isClosed() {
        return battleContainer.getStatus() == BattleStatus.CLOSED;
    }

    public String getWinnerTag() {
        return battleContainer.getWinnerTag();
    }

    public List<BattleProfileContainer> getBattleProfileContainers() {
        return new ArrayList<>(this.battleContainer.getProfileIdBattleProfileContainerMap().values());
    }

    public void surrender(Long profileId) {
        if (isClosed()) {
            return;
        }
        battleContainer.setStatus(BattleStatus.CLOSED);
        Long winnerId = battleContainer.getProfileIdBattleProfileContainerMap().get(profileId).getOpponentId();
        battleContainer.setWinner(winnerId);
        battleContainer.forEachProfile(battleProfileContainer -> {
            Map<String, Object> model = new HashMap<>();
            battleContainer.fillModelClosed(model);
            send(model, Message.BATTLE_CONTENT, battleProfileContainer.getProfileId());
        });
        battleService.disposeManager(this);
    }

    private void prepareTask(Long id) {
        Question question = battleService.prepareQuestion();
        question.setId(id);
        TaskDTO taskDTO = battleService.prepareTaskDTO(question);
        battleContainer.addTask(question, taskDTO);
    }

    public void maybeStart(Long profileId) {
        battleContainer.profileReady(profileId);
        if (battleContainer.isReady()) {
            stateIntro();
        }
    }

    private synchronized void stateIntro() {
        battleContainer.setStatus(BattleStatus.INTRO);
        prepareTask((long) battleContainer.getCurrentTaskIndex() + 1);
        battleContainer.forEachProfile(battleProfileContainer -> {
            Map<String, Object> model = new HashMap<>();
            battleContainer.fillModelIntro(model, battleProfileContainer);
            send(model, Message.BATTLE_CONTENT, battleProfileContainer.getProfileId());
        });
        statePreparingNextTaskAfterIntro();
    }

    private synchronized void statePreparingNextTaskAfterIntro() {
        statePreparingNextTask(INTRO_INTERVAL);
    }

    private synchronized void statePreparingNextTask(Integer interval) {
        Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS)
                .subscribe(aLong -> {
                    battleContainer.setNextTaskDate(Instant.now().plus(NEXT_TASK_INTERVAL, ChronoUnit.MILLIS));
                    battleContainer.setStatus(BattleStatus.PREPARING_NEXT_TASK);
                    Map<String, Object> model = new HashMap<>();
                    battleContainer.fillModelPreparingNextTask(model);
                    battleContainer.forEachProfile(battleProfileContainer -> {
                        send(model, Message.BATTLE_CONTENT, battleProfileContainer.getProfileId());
                    });
                    stateAnswering();
                });
    }

    private synchronized void stateAnswering() {
        Flowable.intervalRange(0L, 1L, NEXT_TASK_INTERVAL, NEXT_TASK_INTERVAL, TimeUnit.MILLISECONDS)
                .subscribe(aLong -> {
                    battleContainer.setEndAnsweringDate(Instant.now().plus(ANSWERING_INTERVAL, ChronoUnit.MILLIS));
                    battleContainer.setStatus(BattleStatus.ANSWERING);
                    Map<String, Object> model = new HashMap<>();
                    battleContainer.fillModelAnswering(model);
                    battleContainer.forEachProfile(battleProfileContainer -> {
                        send(model, Message.BATTLE_CONTENT, battleProfileContainer.getProfileId());
                    });
                    stateAnsweringTimeout();
                });
    }

    private synchronized void stateAnsweringTimeout() {
        answeringTimeoutDisposable = Flowable.intervalRange(0L, 1L, ANSWERING_INTERVAL, ANSWERING_INTERVAL, TimeUnit.MILLISECONDS)
                .subscribe(aLong -> {
                    if (battleContainer.getStatus() != BattleStatus.ANSWERING) {
                        return;
                    }
                    battleContainer.setStatus(BattleStatus.ANSWERING_TIMEOUT);
                    Map<String, Object> model = new HashMap<>();
                    battleContainer.fillModelAnsweringTimeout(model);
                    battleContainer.forEachProfile(battleProfileContainer -> {
                        send(model, Message.BATTLE_CONTENT, battleProfileContainer.getProfileId());
                    });
                    stateAnswering();
                });
    }

    public synchronized Map<String, Object> actualModel(Long profileId) {
        Map<String, Object> model = new HashMap<>();
        BattleProfileContainer battleProfileContainer = battleContainer.getProfileIdBattleProfileContainerMap().get(profileId);
        battleContainer.fillModel(model, battleProfileContainer);
        return model;
    }

    public synchronized void stateAnswered(Long profileId, Map<String, Object> content) {
        if (battleContainer.getStatus() != BattleStatus.ANSWERING) {
            return;
        }
        battleContainer.setStatus(BattleStatus.ANSWERED);
        answeringTimeoutDisposable.dispose();
        battleContainer.setAnsweredProfileId(profileId);
        Boolean isAnswerCorrect = false;
        Long markedAnswerId = null;
        if (content.containsKey("answerId")) {
            markedAnswerId = ((Integer) content.get("answerId")).longValue();
            battleContainer.setMarkedAnswerId(markedAnswerId);
            isAnswerCorrect = battleContainer.findCurrentCorrectAnswerId().equals(markedAnswerId);
        }
        BattleProfileContainer container = battleContainer.getProfileIdBattleProfileContainerMap().get(profileId);
        container.setScore(isAnswerCorrect ? container.getScore() + battleContainer.getCurrentTaskPoints() : container.getScore() - battleContainer.getCurrentTaskPoints());
        battleContainer.setNextTaskDate(Instant.now().plus(NEXT_TASK_INTERVAL, ChronoUnit.MILLIS));
        battleContainer.forEachProfile(battleProfileContainer -> {
            Map<String, Object> model = new HashMap<>();
            battleContainer.fillModelAnswered(model, battleProfileContainer);
            send(model, Message.BATTLE_CONTENT, battleProfileContainer.getProfileId());
        });
        battleContainer.setStatus(BattleStatus.PREPARING_NEXT_TASK);
        battleContainer.increaseCurrentTaskIndex();
        prepareTask((long) battleContainer.getCurrentTaskIndex() + 1);
        stateAnswering();
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
        battleContainer.getProfileIdBattleProfileContainerMap().values().stream().forEach(battleProfileContainer -> {
            Map<String, Object> model = new HashMap<>();
            send(model, Message.BATTLE_READY_FAST, battleProfileContainer.getProfileId());
        });
    }
}
