package com.ww.manager.rival.battle;

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
        addState(new StateIntro(manager)).addOnFlowableEndListener(aLong1 -> {
            phase2();
        }).startFlowable();
    }

    public synchronized void phase1() {
        addState(new StatePreparingNextTask(manager)).addOnFlowableEndListener(aLong2 -> {
            addState(new BattleStateAnswering(manager)).addOnFlowableEndListener(aLong3 -> {
                addState(new BattleStateAnsweringTimeout(manager)).addOnFlowableEndListener(aLong4 -> {
                    phase2();
                }).startFlowable();
            }).startFlowable();
        }).startFlowable();
    }

    public synchronized void phase2() {
        if (manager.isEnd()) {
            addState(new StateClose(manager)).startVoid();
        } else {
            addState(new StateChoosingTaskProps(manager)).addOnFlowableEndListener(aLong5 -> {
                boolean randomChooseTaskProps = manager.getModel().randomChooseTaskProps();
                if (randomChooseTaskProps) {
                    phase1();
                } else {
                    addState(new StateChoosingTaskPropsTimeout(manager)).startVoid();
                    phase1();
                }
            }).startFlowable();
        }
    }

    public synchronized void answer(Long profileId, Map<String, Object> content) {
        dispose();
        addState(new BattleStateAnswered(manager, profileId, content)).addOnFlowableEndListener(aLong -> {
            phase2();
        }).startFlowable();
    }

    public synchronized void chosenTaskProps(Long profileId, Map<String, Object> content) {
        if (addState(new StateChosenTaskProps(manager, profileId, content)).startBoolean()) {
            dispose();
            phase1();
        }
    }

}
