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
        new StateIntro(manager).startFlowable().subscribe(aLong1 -> {
            phase2();
        });
    }

    public synchronized void phase1() {
        new StatePreparingNextTask(manager).startFlowable().subscribe(aLong2 -> {
            activeFlowable = new BattleStateAnswering(manager).startFlowable().subscribe(aLong3 -> {
                new BattleStateAnsweringTimeout(manager).startFlowable().subscribe(aLong4 -> {
                    phase2();
                });
            });
        });
    }

    public synchronized void phase2() {
        if (manager.isEnd()) {
            new StateClose(manager).startVoid();
        } else {
            activeFlowable = new StateChoosingTaskProps(manager).startFlowable().subscribe(aLong5 -> {
                boolean randomChooseTaskProps = manager.getContainer().randomChooseTaskProps();
                if (randomChooseTaskProps) {
                    phase1();
                } else {
                    new StateChoosingTaskPropsTimeout(manager).startVoid();
                    phase1();
                }
            });
        }
    }

    public synchronized void answer(Long profileId, Map<String, Object> content) {
        disposeFlowable();
        new BattleStateAnswered(manager, profileId, content).startFlowable().subscribe(aLong -> {
            phase2();
        });
    }

    public synchronized void chosenTaskProps(Long profileId, Map<String, Object> content) {
        if (new StateChosenTaskProps(manager, profileId, content).startBoolean()) {
            disposeFlowable();
            phase1();
        }
    }

}
