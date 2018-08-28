package com.ww.manager;

import com.ww.model.constant.Category;
import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.constant.rival.task.TaskDifficultyLevel;
import com.ww.model.container.rival.RivalInitContainer;
import com.ww.model.container.rival.battle.BattleContainer;
import com.ww.model.container.rival.battle.BattleProfileContainer;
import com.ww.service.rival.battle.BattleService;
import com.ww.service.social.ProfileConnectionService;
import com.ww.websocket.message.Message;
import io.reactivex.Flowable;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class BattleManager extends RivalManager {

    public static final Integer TASK_COUNT = 5;

    public BattleManager(RivalInitContainer bic, BattleService battleService, ProfileConnectionService profileConnectionService) {
        this.rivalService = battleService;
        this.profileConnectionService = profileConnectionService;
        Long creatorId = bic.getCreatorProfile().getId();
        Long opponentId = bic.getOpponentProfile().getId();
        this.rivalContainer = new BattleContainer();
        this.rivalContainer.addProfile(creatorId, new BattleProfileContainer(bic.getCreatorProfile(), opponentId));
        this.rivalContainer.addProfile(opponentId, new BattleProfileContainer(bic.getOpponentProfile(), creatorId));
    }

    protected Message getMessageReadyFast() {
        return Message.BATTLE_READY_FAST;
    }

    protected Message getMessageContent() {
        return Message.BATTLE_CONTENT;
    }

    protected synchronized void stateAnswering() {
        Flowable.intervalRange(0L, 1L, NEXT_TASK_INTERVAL, NEXT_TASK_INTERVAL, TimeUnit.MILLISECONDS)
                .subscribe(aLong -> {
                    if (isClosed()) {
                        return;
                    }
                    rivalContainer.setEndAnsweringDate(Instant.now().plus(ANSWERING_INTERVAL, ChronoUnit.MILLIS));
                    rivalContainer.setStatus(RivalStatus.ANSWERING);
                    rivalContainer.forEachProfile(rivalProfileContainer -> {
                        Map<String, Object> model = new HashMap<>();
                        rivalContainer.fillModelAnswering(model, rivalProfileContainer);
                        send(model, getMessageContent(), rivalProfileContainer.getProfileId());
                    });
                    stateAnsweringTimeout();
                });
    }

    protected synchronized void stateAnsweringTimeout() {
        answeringTimeoutDisposable = Flowable.intervalRange(0L, 1L, ANSWERING_INTERVAL, ANSWERING_INTERVAL, TimeUnit.MILLISECONDS)
                .subscribe(aLong -> {
                    if (rivalContainer.getStatus() != RivalStatus.ANSWERING) {
                        return;
                    }
                    rivalContainer.setStatus(RivalStatus.ANSWERING_TIMEOUT);
                    rivalContainer.forEachProfile(rivalProfileContainer -> {
                        Map<String, Object> model = new HashMap<>();
                        rivalContainer.fillModelAnsweringTimeout(model, rivalProfileContainer);
                        send(model, getMessageContent(), rivalProfileContainer.getProfileId());
                    });
                    if (rivalContainer.getCurrentTaskIndex() == TASK_COUNT - 1) {
                        stateClose();
                    } else {
                        stateChoosingTaskProps();
                    }
                });
    }

    public synchronized void stateAnswered(Long profileId, Map<String, Object> content) {
        rivalContainer.setStatus(RivalStatus.ANSWERED);
        if (answeringTimeoutDisposable != null && !answeringTimeoutDisposable.isDisposed()) {
            answeringTimeoutDisposable.dispose();
            answeringTimeoutDisposable = null;
        }
        rivalContainer.setAnsweredProfileId(profileId);
        Boolean isAnswerCorrect = false;
        if (content.containsKey("answerId")) {
            Long markedAnswerId = ((Integer) content.get("answerId")).longValue();
            rivalContainer.setMarkedAnswerId(markedAnswerId);
            isAnswerCorrect = rivalContainer.findCurrentCorrectAnswerId().equals(markedAnswerId);
        }
        BattleProfileContainer container = (BattleProfileContainer) rivalContainer.getProfileIdRivalProfileContainerMap().get(profileId);
        container.setScore(isAnswerCorrect ? container.getScore() + rivalContainer.getCurrentTaskPoints() : container.getScore() - rivalContainer.getCurrentTaskPoints());
        rivalContainer.forEachProfile(rivalProfileContainer -> {
            Map<String, Object> model = new HashMap<>();
            rivalContainer.fillModelAnswered(model, rivalProfileContainer);
            send(model, getMessageContent(), rivalProfileContainer.getProfileId());
        });
        if (rivalContainer.getCurrentTaskIndex() == TASK_COUNT - 1) {
            stateClose();
        } else {
            stateChoosingTaskProps();
        }
    }

    public synchronized void stateChoosingTaskProps() {
        Flowable.intervalRange(0L, 1L, SHOWING_ANSWER_INTERVAL, SHOWING_ANSWER_INTERVAL, TimeUnit.MILLISECONDS)
                .subscribe(aLong -> {
                    if (isClosed()) {
                        return;
                    }
                    rivalContainer.setStatus(RivalStatus.CHOOSING_TASK_PROPS);
                    rivalContainer.increaseCurrentTaskIndex();
                    boolean randomChooseTaskProps = rivalContainer.randomChooseTaskProps();
                    if (randomChooseTaskProps) {
                        prepareTask((long) rivalContainer.getCurrentTaskIndex() + 1);
                    }
                    rivalContainer.setEndChoosingTaskPropsDate(Instant.now().plus(CHOOSING_TASK_PROPS_INTERVAL, ChronoUnit.MILLIS));
                    rivalContainer.forEachProfile(rivalProfileContainer -> {
                        Map<String, Object> model = new HashMap<>();
                        rivalContainer.fillModelChoosingTaskProps(model, rivalProfileContainer);
                        send(model, getMessageContent(), rivalProfileContainer.getProfileId());
                    });
                    if (randomChooseTaskProps) {
                        statePreparingNextTask(getRandomChooseTaskPropsInterval());
                    } else {
                        stateChoosingTaskPropsTimeout();
                    }
                });
    }

    public synchronized void stateChosenTaskProps(Long profileId, Map<String, Object> content) {
        if (!rivalContainer.getRivalProfileContainer(profileId).getProfile().getTag().equals(rivalContainer.findChoosingTaskPropsTag())) {
            logger.error("Not choosing profile tried to choose task props, profileId: {}", profileId);
            return;
        }
        rivalContainer.setStatus(RivalStatus.CHOSEN_TASK_PROPS);
        if (choosingTaskPropsTimeoutDisposable != null && !choosingTaskPropsTimeoutDisposable.isDisposed()) {
            choosingTaskPropsTimeoutDisposable.dispose();
            choosingTaskPropsTimeoutDisposable = null;
        }
        Category category = Category.random();
        TaskDifficultyLevel difficultyLevel = TaskDifficultyLevel.random();
        try {
            if (content.containsKey("category")) {
                category = Category.fromString((String) content.get("category"));
            }
            if (content.containsKey("difficultyLevel")) {
                difficultyLevel = TaskDifficultyLevel.fromString((String) content.get("difficultyLevel"));
            }
        } catch (Exception e) {
            logger.error("Wrong content on stateChosenTaskProps for profileId: {}", profileId);
        }
        prepareTask((long) rivalContainer.getCurrentTaskIndex() + 1, category, difficultyLevel);
//        Map<String, Object> model = new HashMap<>();
//        rivalContainer.fillModelChosenTaskProps(model);
//        rivalContainer.forEachProfile(rivalProfileContainer -> {
//            send(model, getMessageContent(), rivalProfileContainer.getProfileId());
//        });
        statePreparingNextTask(0);
    }
}
