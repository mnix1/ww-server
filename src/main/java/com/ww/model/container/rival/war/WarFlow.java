package com.ww.model.container.rival.war;

import com.ww.manager.rival.RivalFlow;
import com.ww.manager.rival.battle.BattleManager;
import com.ww.manager.rival.battle.state.BattleStateAnswered;
import com.ww.manager.rival.battle.state.BattleStateAnswering;
import com.ww.manager.rival.battle.state.BattleStateAnsweringTimeout;
import com.ww.manager.rival.state.*;
import com.ww.manager.rival.war.WarManager;
import com.ww.manager.rival.war.state.*;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

import static com.ww.service.rival.global.RivalMessageService.CHOOSE_WHO_ANSWER;
import static com.ww.service.rival.global.RivalMessageService.HINT;

@Getter
public class WarFlow extends RivalFlow {

    private WarManager manager;

    public WarFlow(WarManager manager) {
        this.manager = manager;
    }

    public boolean processMessage(Long profileId, Map<String, Object> content) {
        if (super.processMessage(profileId, content)) {
            return true;
        }
        String id = (String) content.get("id");
        if (id.equals(CHOOSE_WHO_ANSWER)) {
            chosenWhoAnswer(profileId, content);
        } else if (id.equals(HINT)) {
            hint(profileId, content);
        } else {
            return false;
        }
        return true;
    }

    public synchronized void start() {
        new StateIntro(manager).startFlowable().subscribe(aLong1 -> {
            phase2();
        });
    }

    public synchronized void phase1() {
        new StatePreparingNextTask(manager).startFlowable().subscribe(aLong2 -> {
            activeFlowable = new WarStateAnswering(manager).startFlowable().subscribe(aLong3 -> {
                new WarStateAnsweringTimeout(manager).startFlowable().subscribe(aLong4 -> {
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
                    phase3();
                } else {
                    new StateChoosingTaskPropsTimeout(manager).startVoid();
                    phase3();
                }
            });
        }
    }

    public synchronized void phase3() {
        activeFlowable = new WarStateChoosingWhoAnswer(manager).startFlowable().subscribe(aLong2 -> {
            phase1();
        });
    }

    public synchronized void wisieAnswered(Long profileId, Long answerId) {
        Map<String, Object> content = new HashMap<>();
        content.put("answerId", answerId.intValue());
        answer(profileId, content);
    }

    public synchronized void answer(Long profileId, Map<String, Object> content) {
        disposeFlowable();
        new WarStateAnswered(manager, profileId, content).startFlowable().subscribe(aLong -> {
            phase2();
        });
    }

    public synchronized void hint(Long profileId, Map<String, Object> content) {
        new WarStateHinted(manager, profileId, content).startVoid();
    }

    public synchronized void chosenTaskProps(Long profileId, Map<String, Object> content) {
        if (new StateChosenTaskProps(manager, profileId, content).startBoolean()) {
            disposeFlowable();
            phase3();
        }
    }

    public synchronized void chosenWhoAnswer(Long profileId, Map<String, Object> content) {
        if (new WarStateChosenWhoAnswer(manager, profileId, content).startBoolean()) {
            disposeFlowable();
            phase1();
        }
    }

    public synchronized void surrender(Long profileId) {
        manager.getContainer().stopWisieAnswerManager();
        super.surrender(profileId);
    }

}
