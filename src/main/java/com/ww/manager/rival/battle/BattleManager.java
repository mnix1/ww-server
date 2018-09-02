package com.ww.manager.rival.battle;

import com.ww.manager.rival.RivalManager;
import com.ww.manager.rival.battle.state.BattleStateAnswered;
import com.ww.manager.rival.battle.state.BattleStateAnswering;
import com.ww.manager.rival.battle.state.BattleStateAnsweringTimeout;
import com.ww.manager.rival.state.StateChoosingTaskProps;
import com.ww.manager.rival.state.*;
import com.ww.model.container.rival.RivalInitContainer;
import com.ww.model.container.rival.battle.BattleContainer;
import com.ww.model.container.rival.battle.BattleProfileContainer;
import com.ww.service.rival.battle.BattleService;
import com.ww.service.social.ProfileConnectionService;
import com.ww.websocket.message.Message;

import java.util.Map;

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
        return Message.BATTLE_READY;
    }

    public Message getMessageContent() {
        return Message.BATTLE_CONTENT;
    }

    public boolean isEnd() {
        return rivalContainer.getCurrentTaskIndex() == TASK_COUNT - 1;
    }

    public synchronized void start() {
        new StateIntro(this).startFlowable().subscribe(aLong1 -> {
            phase1();
        });
    }

    public synchronized void phase1() {
        new StatePreparingNextTask(this).startFlowable().subscribe(aLong2 -> {
            answeringTimeoutDisposable = new BattleStateAnswering(this).startFlowable().subscribe(aLong3 -> {
                new BattleStateAnsweringTimeout(this).startFlowable().subscribe(aLong4 -> {
                    phase2();
                });
            });
        });
    }

    public synchronized void phase2() {
        if (isEnd()) {
            new StateClose(this).startVoid();
        } else {
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

    public synchronized void answer(Long profileId, Map<String, Object> content) {
        disposeFlowable();
        new BattleStateAnswered(this, profileId, content).startFlowable().subscribe(aLong -> {
            phase2();
        });
    }

    public synchronized void chosenTaskProps(Long profileId, Map<String, Object> content) {
        if (new StateChosenTaskProps(this, profileId, content).startBoolean()) {
            disposeFlowable();
            phase1();
        }
    }

}
