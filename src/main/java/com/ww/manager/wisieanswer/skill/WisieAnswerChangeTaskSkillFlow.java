package com.ww.manager.wisieanswer.skill;

import com.ww.manager.wisieanswer.WisieAnswerFlow;
import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.skill.state.changetask.WisieStateSubmitsApplication;
import com.ww.manager.wisieanswer.skill.state.changetask.WisieStateWantsToChangeTask;
import lombok.Getter;

@Getter
public class WisieAnswerChangeTaskSkillFlow {
    private WisieAnswerFlow flow;
    private WisieAnswerManager manager;

    private boolean changeTaskUsed = false;

    public WisieAnswerChangeTaskSkillFlow(WisieAnswerFlow flow) {
        this.flow = flow;
        this.manager = flow.getManager();
    }

    private synchronized void phaseChangeTask() {
        flow.addState(new WisieStateWantsToChangeTask(manager)).addOnFlowableEndListener(aLong1 -> {
            flow.addState(new WisieStateSubmitsApplication(manager)).addOnFlowableEndListener(aLong2 -> {
                manager.getWarManager().getFlow().changeTask();
            }).startFlowable();
        }).startFlowable();
    }

    public void changeTask() {
        if (changeTaskUsed) {
            return;
        }
        flow.dispose();
        changeTaskUsed = true;
        phaseChangeTask();
    }

}
