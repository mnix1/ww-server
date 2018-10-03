package com.ww.model.container.rival.war;

import com.ww.manager.rival.RivalFlow;
import com.ww.manager.rival.state.*;
import com.ww.manager.rival.war.WarManager;
import com.ww.manager.rival.war.state.*;
import com.ww.manager.rival.war.state.skill.WarStateHintUsed;
import com.ww.manager.rival.war.state.skill.WarStateKidnappingUsed;
import com.ww.manager.rival.war.state.skill.WarStateLifebuoyUsed;
import com.ww.manager.rival.war.state.skill.WarStateWaterPistolUsed;
import com.ww.model.constant.rival.RivalStatus;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

import static com.ww.service.rival.global.RivalMessageService.*;

@Getter
public class WarFlow extends RivalFlow {

    private WarManager manager;
    private WarSkillFlow skillFlow;

    public WarFlow(WarManager manager) {
        this.manager = manager;
        this.skillFlow = new WarSkillFlow(manager);
    }

    public boolean processMessage(Long profileId, Map<String, Object> content) {
        if (super.processMessage(profileId, content)) {
            return true;
        }
        String id = (String) content.get("id");
        RivalStatus status = getManager().getModel().getStatus();
        if (id.equals(CHOOSE_WHO_ANSWER) && status == RivalStatus.CHOOSING_WHO_ANSWER) {
            chosenWhoAnswer(profileId, content);
        } else {
            return skillFlow.processMessage(profileId, content);
        }
        return true;
    }

    protected synchronized void dispose() {
        manager.getModel().stopWisieAnswerManager();
        super.dispose();
    }

    public synchronized void start() {
        state = new StateIntro(manager).addOnFlowableEndListener(aLong1 -> {
            phase2();
        }).startFlowable();
    }

    public synchronized void phase1() {
        state = new StatePreparingNextTask(manager).addOnFlowableEndListener(aLong2 -> {
            state = new WarStateAnswering(manager).addOnFlowableEndListener(aLong3 -> {
                state = new WarStateAnsweringTimeout(manager).addOnFlowableEndListener(aLong4 -> {
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
                        phase3();
                    } else {
                        new StateChoosingTaskPropsTimeout(manager).startVoid();
                        phase3();
                    }
                }
            }).startFlowable();
        }
    }

    public synchronized void phase3() {
        state = new WarStateChoosingWhoAnswer(manager).addOnFlowableEndListener(aLong2 -> {
            phase1();
        }).startFlowable();
    }

    public synchronized void wisieAnswered(Long profileId, Long answerId) {
        dispose();
        logger.trace(manager.toString() + ", wisieAnswered, profileId={}, answerId={}", profileId, answerId);
        Map<String, Object> content = new HashMap<>();
        content.put("answerId", answerId.intValue());
        answer(profileId, content);
    }

    public synchronized void answer(Long profileId, Map<String, Object> content) {
        dispose();
        state = new WarStateAnswered(manager, profileId, content).addOnFlowableEndListener(aLong -> {
            phase2();
        }).startFlowable();
    }

    public synchronized void chosenTaskProps(Long profileId, Map<String, Object> content) {
        if (new StateChosenTaskProps(manager, profileId, content).startBoolean()) {
            dispose();
            phase3();
        }
    }

    public synchronized void chosenWhoAnswer(Long profileId, Map<String, Object> content) {
        if (new WarStateChosenWhoAnswer(manager, profileId, content).startBoolean()) {
            dispose();
            phase1();
        }
    }

}
