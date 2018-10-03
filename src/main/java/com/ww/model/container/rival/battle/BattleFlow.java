package com.ww.model.container.rival.battle;

import com.ww.manager.rival.RivalFlow;
import com.ww.manager.rival.battle.BattleManager;
import com.ww.manager.rival.battle.state.BattleStateAnswered;
import com.ww.manager.rival.battle.state.BattleStateAnswering;
import com.ww.manager.rival.battle.state.BattleStateAnsweringTimeout;
import com.ww.manager.rival.state.*;
import lombok.Getter;

import java.util.Map;

@Getter
public class BattleFlow extends RivalFlow {

    private BattleManager manager;

    public BattleFlow(BattleManager manager) {
        this.manager = manager;
    }

    public synchronized void start() {
        state = new StateIntro(manager).addOnFlowableEndListener(aLong1 -> {
            phase2();
        }).startFlowable();
    }

    public synchronized void phase1() {
        state = new StatePreparingNextTask(manager).addOnFlowableEndListener(aLong2 -> {
            state = new BattleStateAnswering(manager).addOnFlowableEndListener(aLong3 -> {
                state = new BattleStateAnsweringTimeout(manager).addOnFlowableEndListener(aLong4 -> {
                    phase2();
                }).startFlowable();
            }).startFlowable();
        }).startFlowable();
    }

    public synchronized void phase2() {
        if (manager.isEnd()) {
            new StateClose(manager).startVoid();
        } else {
            state = new StateChoosingTaskProps(manager).addOnFlowableEndListener(aLong5 -> {
                synchronized (this) {
                    boolean randomChooseTaskProps = manager.getModel().randomChooseTaskProps();
                    if (randomChooseTaskProps) {
                        phase1();
                    } else {
                        new StateChoosingTaskPropsTimeout(manager).startVoid();
                        phase1();
                    }
                }
            }).startFlowable();
        }
    }

    public synchronized void answer(Long profileId, Map<String, Object> content) {
        dispose();
        state = new BattleStateAnswered(manager, profileId, content).addOnFlowableEndListener(aLong -> {
            phase2();
        }).startFlowable();
    }

    public synchronized void chosenTaskProps(Long profileId, Map<String, Object> content) {
        if (new StateChosenTaskProps(manager, profileId, content).startBoolean()) {
            dispose();
            phase1();
        }
    }

}
