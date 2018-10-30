package com.ww.manager.rival.war;

import com.ww.manager.rival.RivalFlow;
import com.ww.manager.rival.state.*;
import com.ww.manager.rival.war.skill.WarSkillFlow;
import com.ww.manager.rival.war.state.*;
import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.war.WarTeam;
import com.ww.model.container.rival.war.skill.PassiveSkillsInit;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

import static com.ww.service.rival.global.RivalMessageService.CHOOSE_WHO_ANSWER;

@Getter
public class WarFlow extends RivalFlow {
    protected WarManager manager;
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

    public synchronized void dispose() {
        manager.getModel().stopWisieAnswerManager();
        super.dispose();
    }

    public synchronized void start() {
        for (RivalTeam team : manager.getModel().getTeams().getTeams()) {
            new PassiveSkillsInit((WarTeam) team).init();
        }
        addState(new StateIntro(manager)).addOnFlowableEndListener(aLong1 -> {
            phase2();
        }).startFlowable();
    }

    public synchronized void phase1() {
        addState(new StatePreparingNextTask(manager)).addOnFlowableEndListener(aLong2 -> {
            addState(new WarStateAnswering(manager)).addOnFlowableEndListener(aLong3 -> {
                phase4();
            }).startFlowable();
        }).startFlowable();
    }

    public synchronized void phase2() {
        if (manager.isEnd()) {
            addState(new StateClose(manager)).startVoid();
        } else {
            addState(new WarStateChoosingTaskProps(manager)).addOnFlowableEndListener(aLong5 -> {
                boolean randomChooseTaskProps = manager.getModel().randomChooseTaskProps();
                if (randomChooseTaskProps) {
                    phase3();
                } else {
                    addState(new StateChoosingTaskPropsTimeout(manager)).startVoid();
                    phase3();
                }
            }).startFlowable();
        }
    }

    public synchronized void phase3() {
        addState(new WarStateChoosingWhoAnswer(manager)).addOnFlowableEndListener(aLong2 -> {
            phase1();
        }).startFlowable();
    }

    public synchronized void phase4() {
        addState(new WarStateAnsweringTimeout(manager)).addOnFlowableEndListener(aLong4 -> {
            phase2();
        }).startFlowable();
    }

    public synchronized void wisieAnswered(Long profileId, Long answerId) {
        logger.trace(describe() + manager.describe() + " wisieAnswered, profileId={}, answerId={}", profileId, answerId);
        dispose();
        Map<String, Object> content = new HashMap<>();
        content.put("answerId", answerId.intValue());
        answer(profileId, content);
    }

    public synchronized void kidnapped() {
        logger.trace(describe() + manager.describe() + " kidnapped");
        dispose();
        phase4();
    }

    public synchronized void changeTask() {
        logger.trace(describe() + manager.describe() + " changeTask");
        dispose();
        addState(new WarStateChangingTask(manager)).addOnFlowableEndListener(aLong4 -> {
            addState(new WarStateChoosingTaskProps(manager, true)).addOnFlowableEndListener(aLong5 -> {
                phase1();
            }).startFlowable();
        }).startFlowable();
    }

    public synchronized void ghostScaredAndCaught() {
        logger.trace(describe() + manager.describe() + " ghostScaredAndCaught");
        dispose();
        phase4();
    }

    public synchronized void answer(Long profileId, Map<String, Object> content) {
        logger.trace(describe() + manager.describe() + " answer, profileId={}", profileId);
        dispose();
        addState(new WarStateAnswered(manager, profileId, content)).addOnFlowableEndListener(aLong -> {
            phase2();
        }).startFlowable();
    }

    public synchronized void chosenTaskProps(Long profileId, Map<String, Object> content) {
        logger.trace(describe() + manager.describe() + " chosenTaskProps, profileId={}", profileId);
        if (addState(new StateChosenTaskProps(manager, profileId, content)).startBoolean()) {
            dispose();
            phase3();
        }
    }

    public synchronized void chosenWhoAnswer(Long profileId, Map<String, Object> content) {
        logger.trace(describe() + manager.describe() + " chosenWhoAnswer, profileId={}", profileId);
        if (addState(new WarStateChosenWhoAnswer(manager, profileId, content)).startBoolean()) {
            dispose();
            phase1();
        }
    }
}
