package com.ww.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ww.model.constant.Category;
import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.constant.rival.task.TaskDifficultyLevel;
import com.ww.model.container.rival.RivalContainer;
import com.ww.model.container.rival.RivalInitContainer;
import com.ww.model.container.rival.RivalProfileContainer;
import com.ww.model.dto.rival.task.TaskDTO;
import com.ww.model.entity.rival.task.Question;
import com.ww.service.rival.RivalService;
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

public abstract class RivalManager {
    private static final Logger logger = LoggerFactory.getLogger(RivalManager.class);

    public static final Integer TASK_COUNT = 5;

    private static final Integer ANSWERING_INTERVAL = 45000;
    private static final Integer INTRO_INTERVAL = 18000;
    private static final Integer NEXT_TASK_INTERVAL = 2000;
    private static final Integer SHOWING_ANSWER_INTERVAL = 11000;
    private static final Integer RANDOM_CHOOSE_TASK_PROPS_INTERVAL = 8000;
    private static final Integer CHOOSING_TASK_PROPS_INTERVAL = 14000;

    private RivalContainer rivalContainer;

    private RivalService rivalService;
    private ProfileConnectionService profileConnectionService;

    private Disposable answeringTimeoutDisposable;
    private Disposable choosingTaskPropsTimeoutDisposable;

    protected RivalManager(RivalInitContainer bic, RivalService rivalService, ProfileConnectionService profileConnectionService) {
        Long creatorId = bic.getCreatorProfile().getId();
        Long opponentId = bic.getOpponentProfile().getId();
        this.rivalContainer = new RivalContainer();
        this.rivalContainer.addProfile(creatorId, new RivalProfileContainer(bic.getCreatorProfile(), opponentId));
        this.rivalContainer.addProfile(opponentId, new RivalProfileContainer(bic.getOpponentProfile(), creatorId));
        this.rivalService = rivalService;
        this.profileConnectionService = profileConnectionService;
    }

    public boolean canAnswer() {
        return rivalContainer.getStatus() == RivalStatus.ANSWERING;
    }

    public boolean canChooseTaskProps() {
        return rivalContainer.getStatus() == RivalStatus.CHOOSING_TASK_PROPS;
    }

    public boolean isClosed() {
        return rivalContainer.getStatus() == RivalStatus.CLOSED;
    }

    public String getWinnerTag() {
        return rivalContainer.getWinnerTag();
    }

    public List<RivalProfileContainer> getRivalProfileContainers() {
        return new ArrayList<>(this.rivalContainer.getProfileIdRivalProfileContainerMap().values());
    }

    private void prepareTask(Long id) {
        prepareTask(id, Category.random(), TaskDifficultyLevel.random());
    }

    private void prepareTask(Long id, Category category, TaskDifficultyLevel difficultyLevel) {
        Question question = rivalService.prepareQuestion(category, difficultyLevel);
        question.setId(id);
        TaskDTO taskDTO = rivalService.prepareTaskDTO(question);
        rivalContainer.addTask(question, taskDTO);
    }

    public void maybeStart(Long profileId) {
        rivalContainer.profileReady(profileId);
        if (rivalContainer.isReady()) {
            stateIntro();
        }
    }

    private synchronized void stateIntro() {
        rivalContainer.setStatus(RivalStatus.INTRO);
        prepareTask((long) rivalContainer.getCurrentTaskIndex() + 1);
        rivalContainer.forEachProfile(rivalProfileContainer -> {
            Map<String, Object> model = new HashMap<>();
            rivalContainer.fillModelIntro(model, rivalProfileContainer);
            send(model, getMessageContent(), rivalProfileContainer.getProfileId());
        });
        statePreparingNextTaskAfterIntro();
    }

    private synchronized void statePreparingNextTaskAfterIntro() {
        statePreparingNextTask(INTRO_INTERVAL);
    }

    private synchronized void statePreparingNextTask(Integer interval) {
        Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS)
                .subscribe(aLong -> {
                    if(isClosed()){
                        return;
                    }
                    rivalContainer.setNextTaskDate(Instant.now().plus(NEXT_TASK_INTERVAL, ChronoUnit.MILLIS));
                    rivalContainer.setStatus(RivalStatus.PREPARING_NEXT_TASK);
                    Map<String, Object> model = new HashMap<>();
                    rivalContainer.fillModelPreparingNextTask(model);
                    rivalContainer.forEachProfile(rivalProfileContainer -> {
                        send(model, getMessageContent(), rivalProfileContainer.getProfileId());
                    });
                    stateAnswering();
                });
    }

    private synchronized void stateAnswering() {
        Flowable.intervalRange(0L, 1L, NEXT_TASK_INTERVAL, NEXT_TASK_INTERVAL, TimeUnit.MILLISECONDS)
                .subscribe(aLong -> {
                    if(isClosed()){
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

    private synchronized void stateAnsweringTimeout() {
        answeringTimeoutDisposable = Flowable.intervalRange(0L, 1L, ANSWERING_INTERVAL, ANSWERING_INTERVAL, TimeUnit.MILLISECONDS)
                .subscribe(aLong -> {
                    if (rivalContainer.getStatus() != RivalStatus.ANSWERING) {
                        return;
                    }
                    rivalContainer.setStatus(RivalStatus.ANSWERING_TIMEOUT);
                    Map<String, Object> model = new HashMap<>();
                    rivalContainer.fillModelAnsweringTimeout(model);
                    rivalContainer.forEachProfile(rivalProfileContainer -> {
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
        RivalProfileContainer container = rivalContainer.getProfileIdRivalProfileContainerMap().get(profileId);
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

    public synchronized void stateClose() {
        Flowable.intervalRange(0L, 1L, SHOWING_ANSWER_INTERVAL, SHOWING_ANSWER_INTERVAL, TimeUnit.MILLISECONDS)
                .subscribe(aLong -> {
                    if(isClosed()){
                        return;
                    }
                    rivalContainer.setStatus(RivalStatus.CLOSED);
                    String winnerTag = rivalContainer.findWinnerTag();
                    rivalContainer.setWinnerTag(winnerTag);
                    rivalContainer.setResigned(false);
                    rivalContainer.forEachProfile(rivalProfileContainer -> {
                        Map<String, Object> model = new HashMap<>();
                        rivalContainer.fillModelClosed(model, rivalProfileContainer);
                        send(model, getMessageContent(), rivalProfileContainer.getProfileId());
                    });
                    rivalService.disposeManager(this);
                });
    }

    public void surrender(Long profileId) {
        if (isClosed()) {
            return;
        }
        rivalContainer.setStatus(RivalStatus.CLOSED);
        Long winnerId = rivalContainer.getProfileIdRivalProfileContainerMap().get(profileId).getOpponentId();
        rivalContainer.setWinner(winnerId);
        rivalContainer.setResigned(true);
        rivalContainer.forEachProfile(rivalProfileContainer -> {
            Map<String, Object> model = new HashMap<>();
            rivalContainer.fillModelClosed(model, rivalProfileContainer);
            send(model, getMessageContent(), rivalProfileContainer.getProfileId());
        });
        rivalService.disposeManager(this);
    }


    public synchronized void stateChoosingTaskProps() {
        Flowable.intervalRange(0L, 1L, SHOWING_ANSWER_INTERVAL, SHOWING_ANSWER_INTERVAL, TimeUnit.MILLISECONDS)
                .subscribe(aLong -> {
                    if(isClosed()){
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
                        statePreparingNextTask(RANDOM_CHOOSE_TASK_PROPS_INTERVAL);
                    } else {
                        stateChoosingTaskPropsTimeout();
                    }
                });
    }

    public synchronized void stateChoosingTaskPropsTimeout() {
        choosingTaskPropsTimeoutDisposable = Flowable.intervalRange(0L, 1L, CHOOSING_TASK_PROPS_INTERVAL, CHOOSING_TASK_PROPS_INTERVAL, TimeUnit.MILLISECONDS)
                .subscribe(aLong -> {
                    if (rivalContainer.getStatus() != RivalStatus.CHOOSING_TASK_PROPS) {
                        return;
                    }
                    rivalContainer.setStatus(RivalStatus.CHOOSING_TASK_PROPS_TIMEOUT);
                    prepareTask((long) rivalContainer.getCurrentTaskIndex() + 1);
                    Map<String, Object> model = new HashMap<>();
                    rivalContainer.fillModelChoosingTaskPropsTimeout(model);
                    rivalContainer.forEachProfile(rivalProfileContainer -> {
                        send(model, getMessageContent(), rivalProfileContainer.getProfileId());
                    });
                    statePreparingNextTask(RANDOM_CHOOSE_TASK_PROPS_INTERVAL);
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

    protected abstract Message getMessageReadyFast();

    protected abstract Message getMessageContent();

    public void sendReadyFast() {
        rivalContainer.getProfileIdRivalProfileContainerMap().values().stream().forEach(rivalProfileContainer -> {
            Map<String, Object> model = new HashMap<>();
            send(model, getMessageReadyFast(), rivalProfileContainer.getProfileId());
        });
    }
}
