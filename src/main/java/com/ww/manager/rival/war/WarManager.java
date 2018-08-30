package com.ww.manager.rival.war;

import com.ww.manager.rival.RivalManager;
import com.ww.manager.rival.state.*;
import com.ww.manager.rival.war.state.WarStateAnswered;
import com.ww.manager.rival.war.state.WarStateAnswering;
import com.ww.manager.rival.war.state.WarStateAnsweringTimeout;
import com.ww.model.container.rival.RivalInitContainer;
import com.ww.model.container.rival.RivalProfileContainer;
import com.ww.model.container.rival.war.WarContainer;
import com.ww.model.container.rival.war.WarProfileContainer;
import com.ww.service.rival.war.WarService;
import com.ww.service.social.ProfileConnectionService;
import com.ww.websocket.message.Message;

import java.util.HashMap;
import java.util.Map;

public class WarManager extends RivalManager {

    public static final int PROFILE_ACTIVE_INDEX = 0;
    public WarContainer warContainer;

    public Integer getIntroInterval() {
        return 20500;
//        return 1000;
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

    public boolean isEnd() {
        for (RivalProfileContainer rivalProfileContainer : getRivalProfileContainers()) {
            WarProfileContainer warProfileContainer = (WarProfileContainer) rivalProfileContainer;
            if (warProfileContainer.getPresentIndexes().size() == 0) {
                return true;
            }
        }
        return false;
    }

    public synchronized void start() {
        new StateIntro(this).startFlowable().subscribe(aLong1 -> {
            phase1();
        });
    }

    public synchronized void phase1() {
        new StatePreparingNextTask(this).startFlowable().subscribe(aLong2 -> {
            answeringTimeoutDisposable = new WarStateAnswering(this).startFlowable().subscribe(aLong3 -> {
                new WarStateAnsweringTimeout(this).startFlowable().subscribe(aLong4 -> {
                    phase2();
                });
            });
        });
    }

    public synchronized void phase2() {
        if (isEnd()) {
            new StateClose(this).startVoid();
        } else {
            for (RivalProfileContainer rivalProfileContainer : getRivalProfileContainers()) {
                WarProfileContainer warProfileContainer = (WarProfileContainer) rivalProfileContainer;
                warProfileContainer.randomActiveIndex(rivalContainer.getCurrentTaskIndex());
            }
            choosingTaskPropsDisposable = new StateChoosingTaskProps(this).startFlowable().subscribe(aLong5 -> {
                boolean randomChooseTaskProps = rivalContainer.randomChooseTaskProps();
                if (randomChooseTaskProps) {
                    phase1();
                } else {
                    new StateChoosingTaskPropsTimeout(this).startVoid();
                    phase1();
                }
            });
        }
    }

    public synchronized void heroAnswered(Long profileId, Long answerId) {
        Map<String, Object> content = new HashMap<>();
        content.put("answerId", answerId.intValue());
        answer(profileId, content);
    }

    public synchronized void answer(Long profileId, Map<String, Object> content) {
        disposeFlowable();
        new WarStateAnswered(this, profileId, content).startFlowable().subscribe(aLong -> {
            phase2();
        });
    }

    public synchronized void surrender(Long profileId) {
        warContainer.stopHeroAnswerManager();
        super.surrender(profileId);
    }

    public synchronized void chosenTaskProps(Long profileId, Map<String, Object> content) {
        if (new StateChosenTaskProps(this, profileId, content).startBoolean()) {
            disposeFlowable();
            phase1();
        }
    }

}
