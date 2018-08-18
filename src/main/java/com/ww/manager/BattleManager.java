package com.ww.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ww.model.constant.Category;
import com.ww.model.constant.rival.battle.BattleStatus;
import com.ww.model.constant.rival.task.TaskDifficultyLevel;
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
    public static final Integer TASK_COUNT = 5;

    private static final Integer ANSWERING_INTERVAL = 45000;
    private static final Integer INTRO_INTERVAL = 18000;
    private static final Integer NEXT_TASK_INTERVAL = 4000;
    private static final Integer SHOWING_ANSWER_INTERVAL = 11000;
    private static final Integer RANDOM_CHOOSE_TASK_PROPS_INTERVAL = 8000;
    private static final Integer CHOOSING_TASK_PROPS_INTERVAL = 14000;

    private BattleContainer battleContainer;

    private BattleService battleService;
    private ProfileConnectionService profileConnectionService;

    private Disposable answeringTimeoutDisposable;
    private Disposable choosingTaskPropsTimeoutDisposable;

    public BattleManager(BattleFriendContainer bic, BattleService battleService, ProfileConnectionService profileConnectionService) {
        Long creatorId = bic.getCreatorProfile().getId();
        Long opponentId = bic.getOpponentProfile().getId();
        this.battleContainer = new BattleContainer();
        this.battleContainer.addProfile(creatorId, new BattleProfileContainer(bic.getCreatorProfile(), opponentId));
        this.battleContainer.addProfile(opponentId, new BattleProfileContainer(bic.getOpponentProfile(), creatorId));
        this.battleService = battleService;
        this.profileConnectionService = profileConnectionService;
    }

    public boolean canAnswer() {
        return battleContainer.getStatus() == BattleStatus.ANSWERING;
    }

    public boolean canChooseTaskProps() {
        return battleContainer.getStatus() == BattleStatus.CHOOSING_TASK_PROPS;
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

    private void prepareTask(Long id) {
        prepareTask(id, Category.random(), TaskDifficultyLevel.random());
    }

    private void prepareTask(Long id, Category category, TaskDifficultyLevel difficultyLevel) {
        Question question = battleService.prepareQuestion(category, difficultyLevel);
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
                    battleContainer.forEachProfile(battleProfileContainer -> {
                        Map<String, Object> model = new HashMap<>();
                        battleContainer.fillModelAnswering(model, battleProfileContainer);
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
                    stateChoosingTaskProps();
                });
    }

    public synchronized void stateAnswered(Long profileId, Map<String, Object> content) {
        battleContainer.setStatus(BattleStatus.ANSWERED);
        if (answeringTimeoutDisposable != null && !answeringTimeoutDisposable.isDisposed()) {
            answeringTimeoutDisposable.dispose();
            answeringTimeoutDisposable = null;
        }
        battleContainer.setAnsweredProfileId(profileId);
        Boolean isAnswerCorrect = false;
        if (content.containsKey("answerId")) {
            Long markedAnswerId = ((Integer) content.get("answerId")).longValue();
            battleContainer.setMarkedAnswerId(markedAnswerId);
            isAnswerCorrect = battleContainer.findCurrentCorrectAnswerId().equals(markedAnswerId);
        }
        BattleProfileContainer container = battleContainer.getProfileIdBattleProfileContainerMap().get(profileId);
        container.setScore(isAnswerCorrect ? container.getScore() + battleContainer.getCurrentTaskPoints() : container.getScore() - battleContainer.getCurrentTaskPoints());
        battleContainer.forEachProfile(battleProfileContainer -> {
            Map<String, Object> model = new HashMap<>();
            battleContainer.fillModelAnswered(model, battleProfileContainer);
            send(model, Message.BATTLE_CONTENT, battleProfileContainer.getProfileId());
        });
        if (battleContainer.getCurrentTaskIndex() == TASK_COUNT - 1) {
            stateClose();
        } else {
            stateChoosingTaskProps();
        }
    }

    public synchronized void stateClose() {
        Flowable.intervalRange(0L, 1L, SHOWING_ANSWER_INTERVAL, SHOWING_ANSWER_INTERVAL, TimeUnit.MILLISECONDS)
                .subscribe(aLong -> {
                    battleContainer.setStatus(BattleStatus.CLOSED);
                    String winnerTag = battleContainer.findWinnerTag();
                    battleContainer.setWinnerTag(winnerTag);
                    battleContainer.setResigned(false);
                    battleContainer.forEachProfile(battleProfileContainer -> {
                        Map<String, Object> model = new HashMap<>();
                        battleContainer.fillModelClosed(model, battleProfileContainer);
                        send(model, Message.BATTLE_CONTENT, battleProfileContainer.getProfileId());
                    });
                    battleService.disposeManager(this);
                });
    }

    public void surrender(Long profileId) {
        if (isClosed()) {
            return;
        }
        battleContainer.setStatus(BattleStatus.CLOSED);
        Long winnerId = battleContainer.getProfileIdBattleProfileContainerMap().get(profileId).getOpponentId();
        battleContainer.setWinner(winnerId);
        battleContainer.setResigned(true);
        battleContainer.forEachProfile(battleProfileContainer -> {
            Map<String, Object> model = new HashMap<>();
            battleContainer.fillModelClosed(model, battleProfileContainer);
            send(model, Message.BATTLE_CONTENT, battleProfileContainer.getProfileId());
        });
        battleService.disposeManager(this);
    }


    public synchronized void stateChoosingTaskProps() {
        Flowable.intervalRange(0L, 1L, SHOWING_ANSWER_INTERVAL, SHOWING_ANSWER_INTERVAL, TimeUnit.MILLISECONDS)
                .subscribe(aLong -> {
                    battleContainer.setStatus(BattleStatus.CHOOSING_TASK_PROPS);
                    battleContainer.increaseCurrentTaskIndex();
                    boolean randomChooseTaskProps = battleContainer.randomChooseTaskProps();
                    if (randomChooseTaskProps) {
                        prepareTask((long) battleContainer.getCurrentTaskIndex() + 1);
                    }
                    battleContainer.setEndChoosingTaskPropsDate(Instant.now().plus(CHOOSING_TASK_PROPS_INTERVAL, ChronoUnit.MILLIS));
                    battleContainer.forEachProfile(battleProfileContainer -> {
                        Map<String, Object> model = new HashMap<>();
                        battleContainer.fillModelChoosingTaskProps(model, battleProfileContainer);
                        send(model, Message.BATTLE_CONTENT, battleProfileContainer.getProfileId());
                    });
                    if (randomChooseTaskProps) {
                        statePreparingNextTask(RANDOM_CHOOSE_TASK_PROPS_INTERVAL);
                    } else {
                        stateChoosingTaskPropsTimeout();
                    }
                });
    }

    public synchronized void stateChoosingTaskPropsTimeout() {
        choosingTaskPropsTimeoutDisposable = Flowable.intervalRange(0L, 1L, CHOOSING_TASK_PROPS_INTERVAL, CHOOSING_TASK_PROPS_INTERVAL, TimeUnit.MILLISECONDS)
                .subscribe(aLong -> {
                    if (battleContainer.getStatus() != BattleStatus.CHOOSING_TASK_PROPS) {
                        return;
                    }
                    battleContainer.setStatus(BattleStatus.CHOOSING_TASK_PROPS_TIMEOUT);
                    prepareTask((long) battleContainer.getCurrentTaskIndex() + 1);
                    Map<String, Object> model = new HashMap<>();
                    battleContainer.fillModelChoosingTaskPropsTimeout(model);
                    battleContainer.forEachProfile(battleProfileContainer -> {
                        send(model, Message.BATTLE_CONTENT, battleProfileContainer.getProfileId());
                    });
                    statePreparingNextTask(RANDOM_CHOOSE_TASK_PROPS_INTERVAL);
                });
    }

    public synchronized void stateChosenTaskProps(Long profileId, Map<String, Object> content) {
        if (!battleContainer.getBattleProfileContainer(profileId).getProfile().getTag().equals(battleContainer.findChoosingTaskPropsTag())) {
            logger.error("Not choosing profile tried to choose task props, profileId: {}", profileId);
            return;
        }
        battleContainer.setStatus(BattleStatus.CHOSEN_TASK_PROPS);
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
        prepareTask((long) battleContainer.getCurrentTaskIndex() + 1, category, difficultyLevel);
//        Map<String, Object> model = new HashMap<>();
//        battleContainer.fillModelChosenTaskProps(model);
//        battleContainer.forEachProfile(battleProfileContainer -> {
//            send(model, Message.BATTLE_CONTENT, battleProfileContainer.getProfileId());
//        });
        statePreparingNextTask(0);
    }

    public synchronized Map<String, Object> actualModel(Long profileId) {
        Map<String, Object> model = new HashMap<>();
        BattleProfileContainer battleProfileContainer = battleContainer.getProfileIdBattleProfileContainerMap().get(profileId);
        battleContainer.fillModel(model, battleProfileContainer);
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
        battleContainer.getProfileIdBattleProfileContainerMap().values().stream().forEach(battleProfileContainer -> {
            Map<String, Object> model = new HashMap<>();
            send(model, Message.BATTLE_READY_FAST, battleProfileContainer.getProfileId());
        });
    }
}
