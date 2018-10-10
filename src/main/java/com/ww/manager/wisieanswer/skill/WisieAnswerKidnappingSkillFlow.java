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

    public WisieAnswerKidnappingSkillFlow(WisieAnswerFlow flow) {
        this.flow = flow;
        this.manager = flow.getManager();
    }

    private synchronized void phaseKidnapping(WisieAnswerManager opponentManager) {
        AbstractState prevState = flow.lastFlowableState();
        flow.addSkillState(new WisieStatePreparingKidnapping(manager)).setPrevState(prevState).addOnFlowableEndListener(aLong1 -> {
            opponentManager.getFlow().dispose();
            AbstractState opponentPrevState = opponentManager.getFlow().lastFlowableState();
            WisieStateTryingToKidnap kidnapTryState = new WisieStateTryingToKidnap(manager, opponentManager);
            flow.addState(kidnapTryState);
            boolean success = kidnapTryState.calculateSuccess();
            kidnapTryState.setSuccess(success);
            kidnapTryState.addOnFlowableEndListener(aLong2 -> {
                if (success) {
                    flow.addState(new WisieStateKidnappingSucceeded(manager, opponentManager)).addOnFlowableEndListener(aLong3 -> {
                        manager.getWarManager().getFlow().kidnapped();
                    }).startFlowable();
                } else {
                    opponentManager.getFlow().addState(new WisieStateWasNotKidnapped(manager)).addOnFlowableEndListener(aLong3 -> {
                        opponentPrevState.startFlowableEndListeners();
                    }).startFlowable();
                    flow.addState(new WisieStateKidnappingFailed(manager, opponentManager)).addOnFlowableEndListener(aLong3 -> {
                        flow.addState(new WisieStateChangingClothes(manager)).addOnFlowableEndListener(aLong4 -> {
                            flow.addState(new WisieStateContinueAfterKidnapping(manager)).startVoid();
                            prevState.startFlowableEndListeners();
                        }).startFlowable();
                    }).startFlowable();
                }
            }).startFlowable();
        }).startFlowable();
    }

    public void kidnapping(WisieAnswerManager opponentManager) {
        if (kidnappingUsed) {
            return;
        }
        flow.dispose();
        kidnappingUsed = true;
        phaseKidnapping(opponentManager);
    }

}
