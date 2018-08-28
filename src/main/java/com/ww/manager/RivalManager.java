package com.ww.manager;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ww.model.constant.Category;
import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.constant.rival.task.TaskDifficultyLevel;
import com.ww.model.container.rival.RivalContainer;
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
    protected static final Logger logger = LoggerFactory.getLogger(RivalManager.class);

    public static final String DRAW_WINNER_TAG = "";

    protected Integer ANSWERING_INTERVAL = 45000;
    protected Integer NEXT_TASK_INTERVAL = 2000;
    protected Integer SHOWING_ANSWER_INTERVAL = 10000;
    protected Integer CHOOSING_TASK_PROPS_INTERVAL = 14000;

    public RivalContainer rivalContainer;

    protected RivalService rivalService;

    protected ProfileConnectionService profileConnectionService;

    protected Disposable answeringTimeoutDisposable;
    protected Disposable choosingTaskPropsTimeoutDisposable;


    protected Integer getIntroInterval() {
        return 9500;
    }
    protected Integer getRandomChooseTaskPropsInterval() {
        return 6000;
    }

    protected abstract Message getMessageReadyFast();

    protected abstract Message getMessageContent();

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

    protected void prepareTask(Long id) {
        prepareTask(id, Category.random(), TaskDifficultyLevel.random());
    }

    protected void prepareTask(Long id, Category category, TaskDifficultyLevel difficultyLevel) {
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

    protected synchronized void stateIntro() {
        rivalContainer.setStatus(RivalStatus.INTRO);
        prepareTask((long) rivalContainer.getCurrentTaskIndex() + 1);
        rivalContainer.forEachProfile(rivalProfileContainer -> {
            Map<String, Object> model = new HashMap<>();
            rivalContainer.fillModelIntro(model, rivalProfileContainer);
            send(model, getMessageContent(), rivalProfileContainer.getProfileId());
        });
        statePreparingNextTaskAfterIntro();
    }

    protected synchronized void statePreparingNextTaskAfterIntro() {
        statePreparingNextTask(getIntroInterval());
    }

    protected synchronized void statePreparingNextTask(Integer interval) {
        Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS)
                .subscribe(aLong -> {
                    if (isClosed()) {
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

    protected abstract void stateAnswering();

    protected abstract void stateAnsweringTimeout();

    public abstract void stateAnswered(Long profileId, Map<String, Object> content);

    public synchronized void stateClose() {
        Flowable.intervalRange(0L, 1L, SHOWING_ANSWER_INTERVAL, SHOWING_ANSWER_INTERVAL, TimeUnit.MILLISECONDS)
                .subscribe(aLong -> {
                    if (isClosed()) {
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

    public abstract void stateChoosingTaskProps();

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
                    statePreparingNextTask(getRandomChooseTaskPropsInterval());
                });
    }

    public abstract void stateChosenTaskProps(Long profileId, Map<String, Object> content);

    public synchronized Map<String, Object> actualModel(Long profileId) {
        Map<String, Object> model = new HashMap<>();
        RivalProfileContainer rivalProfileContainer = rivalContainer.getProfileIdRivalProfileContainerMap().get(profileId);
        rivalContainer.fillModel(model, rivalProfileContainer);
        return model;
    }

    public synchronized void surrender(Long profileId) {
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
}
