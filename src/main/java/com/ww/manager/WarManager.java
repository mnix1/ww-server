package com.ww.manager;

import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.container.rival.RivalInitContainer;
import com.ww.model.container.rival.RivalProfileContainer;
import com.ww.model.container.rival.war.WarContainer;
import com.ww.model.container.rival.war.WarProfileContainer;
import com.ww.service.rival.war.WarService;
import com.ww.service.social.ProfileConnectionService;
import com.ww.websocket.message.Message;
import io.reactivex.Flowable;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class WarManager extends RivalManager {

    public static final int PROFILE_ACTIVE_INDEX = 0;
    private WarContainer warContainer;

    protected Integer getIntroInterval() {
        return 20500;
//        return 1000;
    }

    protected Integer getRandomChooseTaskPropsInterval() {
//        return 1000;
        return 16000;
    }

    public WarManager(RivalInitContainer bic, WarService warService, ProfileConnectionService profileConnectionService) {
        this.rivalService = warService;
        this.profileConnectionService = profileConnectionService;
        Long creatorId = bic.getCreatorProfile().getId();
        Long opponentId = bic.getOpponentProfile().getId();
        this.rivalContainer = new WarContainer();
        this.rivalContainer.addProfile(creatorId, new WarProfileContainer(bic.getCreatorProfile(), warService.getProfileHeroes(creatorId), opponentId));
        this.rivalContainer.addProfile(opponentId, new WarProfileContainer(bic.getOpponentProfile(), warService.getProfileHeroes(opponentId), creatorId));
        this.warContainer = (WarContainer) this.rivalContainer;
    }

    protected Message getMessageReadyFast() {
        return Message.WAR_READY_FAST;
    }

    public Message getMessageContent() {
        return Message.WAR_CONTENT;
    }

    protected synchronized void stateAnswering() {
        Flowable.intervalRange(0L, 1L, NEXT_TASK_INTERVAL, NEXT_TASK_INTERVAL, TimeUnit.MILLISECONDS)
                .subscribe(aLong -> {
                    if (isClosed()) {
                        return;
                    }
                    rivalContainer.setEndAnsweringDate(Instant.now().plus(ANSWERING_INTERVAL, ChronoUnit.MILLIS));
                    rivalContainer.setStatus(RivalStatus.ANSWERING);

                    warContainer.updateHeroAnswerManagers(this);

                    rivalContainer.forEachProfile(rivalProfileContainer -> {
                        Map<String, Object> model = new HashMap<>();
                        rivalContainer.fillModelAnswering(model, rivalProfileContainer);
                        send(model, getMessageContent(), rivalProfileContainer.getProfileId());
                    });
                    stateAnsweringTimeout();
                    warContainer.startHeroAnswerManager();
                });
    }

    protected synchronized void stateAnsweringTimeout() {
        answeringTimeoutDisposable = Flowable.intervalRange(0L, 1L, ANSWERING_INTERVAL, ANSWERING_INTERVAL, TimeUnit.MILLISECONDS)
                .subscribe(aLong -> {
                    if (rivalContainer.getStatus() != RivalStatus.ANSWERING) {
                        return;
                    }
                    warContainer.stopHeroAnswerManager();
                    boolean end = false;
                    for (RivalProfileContainer rivalProfileContainer : getRivalProfileContainers()) {
                        WarProfileContainer warProfileContainer = (WarProfileContainer) rivalProfileContainer;
                        warProfileContainer.removeActiveIndexFromPresentIndexes();
                        if (warProfileContainer.getPresentIndexes().size() == 0) {
                            end = true;
                        }
                    }
                    rivalContainer.setStatus(RivalStatus.ANSWERING_TIMEOUT);
                    rivalContainer.forEachProfile(rivalProfileContainer -> {
                        Map<String, Object> model = new HashMap<>();
                        rivalContainer.fillModelAnsweringTimeout(model, rivalProfileContainer);
                        send(model, getMessageContent(), rivalProfileContainer.getProfileId());
                    });
                    if (end) {
                        stateClose();
                    } else {
                        stateChoosingTaskProps();
                    }
                });
    }

    public synchronized void heroAnswered(Long profileId, Long answerId) {
        Map<String, Object> content = new HashMap<>();
        content.put("answerId", answerId.intValue());
        stateAnswered(profileId, content);
    }

    public synchronized void stateAnswered(Long profileId, Map<String, Object> content) {
        rivalContainer.setStatus(RivalStatus.ANSWERED);
        warContainer.stopHeroAnswerManager();
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
        WarProfileContainer container = (WarProfileContainer) rivalContainer.getProfileIdRivalProfileContainerMap().get(profileId);
        if (isAnswerCorrect) {
            container = (WarProfileContainer) rivalContainer.getProfileIdRivalProfileContainerMap().get(container.getOpponentId());
        }
        container.removeActiveIndexFromPresentIndexes();
        rivalContainer.forEachProfile(rivalProfileContainer -> {
            Map<String, Object> model = new HashMap<>();
            rivalContainer.fillModelAnswered(model, rivalProfileContainer);
            send(model, getMessageContent(), rivalProfileContainer.getProfileId());
        });
        if (container.getPresentIndexes().size() == 0) {
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
                    prepareTask((long) rivalContainer.getCurrentTaskIndex() + 1);
                    rivalContainer.setEndChoosingTaskPropsDate(Instant.now().plus(CHOOSING_TASK_PROPS_INTERVAL, ChronoUnit.MILLIS));
                    for (RivalProfileContainer rivalProfileContainer : getRivalProfileContainers()) {
                        WarProfileContainer warProfileContainer = (WarProfileContainer) rivalProfileContainer;
                        warProfileContainer.randomActiveIndex(rivalContainer.getCurrentTaskIndex());
                    }
                    rivalContainer.forEachProfile(rivalProfileContainer -> {
                        Map<String, Object> model = new HashMap<>();
                        rivalContainer.fillModelChoosingTaskProps(model, rivalProfileContainer);
                        send(model, getMessageContent(), rivalProfileContainer.getProfileId());
                    });
                    statePreparingNextTask(getRandomChooseTaskPropsInterval());
                });
    }

    public synchronized void stateChosenTaskProps(Long profileId, Map<String, Object> content) {
        return;
    }

    public synchronized void surrender(Long profileId) {
        warContainer.stopHeroAnswerManager();
        super.surrender(profileId);
    }
}
