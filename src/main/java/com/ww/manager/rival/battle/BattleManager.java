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
import com.ww.service.rival.battle.RivalBattleService;
import com.ww.service.social.ProfileConnectionService;

import java.util.Map;

public class BattleManager extends RivalManager {

    public static final Integer TASK_COUNT = 5;

    public BattleManager(RivalInitContainer container, RivalBattleService rivalBattleService, ProfileConnectionService profileConnectionService) {
        this.rivalService = rivalBattleService;
        this.profileConnectionService = profileConnectionService;
        Long creatorId = container.getCreatorProfile().getId();
        Long opponentId = container.getOpponentProfile().getId();
        this.rivalContainer = new BattleContainer();
        this.rivalContainer.storeInformationFromInitContainer(container);
        this.rivalContainer.addProfile(creatorId, new BattleProfileContainer(container.getCreatorProfile(), opponentId));
        this.rivalContainer.addProfile(opponentId, new BattleProfileContainer(container.getOpponentProfile(), creatorId));
    }

    public boolean isEnd() {
        return rivalContainer.getCurrentTaskIndex() == TASK_COUNT - 1;
    }

    public synchronized void start() {
        new StateIntro(this).startFlowable().subscribe(aLong1 -> {
            phase2();
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
