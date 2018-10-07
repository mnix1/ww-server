package com.ww.manager.wisieanswer.skill;

import com.ww.manager.AbstractState;
import com.ww.manager.wisieanswer.WisieAnswerFlow;
import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.skill.state.kidnapping.*;
import lombok.Getter;

@Getter
public class WisieAnswerKidnappingSkillFlow {
    private WisieAnswerFlow flow;
    private WisieAnswerManager manager;

    private boolean kidnappingUsed = false;
    private boolean kidnappingUsedOnIt = false;

    public WisieAnswerKidnappingSkillFlow(WisieAnswerFlow flow) {
        this.flow = flow;
        this.manager = flow.getManager();
    }

    private synchronized void phaseKidnapping(WisieAnswerManager opponent) {
        AbstractState prevState = flow.lastFlowableState();
        flow.addState(new WisieStatePreparingKidnapping(manager)).addOnFlowableEndListener(aLong1 -> {
                WisieStateTryingToKidnap kidnapTryState = new WisieStateTryingToKidnap(manager, opponent);
                flow.addState(kidnapTryState);
                boolean success = kidnapTryState.calculateSuccess();
                long interval = kidnapTryState.calculateInterval();
                kidnapTryState.setSuccess(success);
                kidnapTryState.setInterval(interval);
                kidnapTryState.addOnFlowableEndListener(aLong2 -> {
                    if (success) {
                        flow.addState(new WisieStateKidnappingSucceeded(manager, opponent)).addOnFlowableEndListener(aLong3 -> {
                            manager.getManager().getFlow().kidnapped();
                        }).startFlowable();
                    } else {
                        flow.addState(new WisieStateKidnappingFailed(manager, opponent)).addOnFlowableEndListener(aLong3 -> {
                            flow.addState(new WisieStateChangingClothes(manager)).addOnFlowableEndListener(aLong4 -> {
                                flow.addState(new WisieStateContinueAfterKidnapping(manager)).startVoid();
                                prevState.startFlowableEndListeners();
                            }).startFlowable();
                        }).startFlowable();
                    }
                }).startFlowable();
        }).startFlowable();
    }

    public void kidnapping(WisieAnswerManager opponent) {
        if (kidnappingUsed) {
            return;
        }
        flow.dispose();
        kidnappingUsed = true;
        phaseKidnapping(opponent);
    }

    private synchronized void phaseKidnappingUsedOnIt(boolean success, long interval) {
        AbstractState prevState = flow.lastFlowableState();
        flow.addState(new WisieStateTryingToDefend(manager, interval)).addOnFlowableEndListener(aLong1 -> {
            if (success) {
                flow.addState(new WisieStateWasKidnapped(manager)).startVoid();
            } else {
                flow.addState(new WisieStateWasNotKidnapped(manager)).addOnFlowableEndListener(aLong3 -> {
                    prevState.startFlowableEndListeners();
                }).startFlowable();
            }
        }).startFlowable();
    }

    public void kidnappingUsedOnIt(boolean success, long interval) {
        if (kidnappingUsedOnIt) {
            return;
        }
        flow.dispose();
        kidnappingUsedOnIt = true;
        phaseKidnappingUsedOnIt(success, interval);
    }

}
