package com.ww.manager.wisieanswer.skill;

import com.ww.manager.AbstractState;
import com.ww.manager.wisieanswer.WisieAnswerFlow;
import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.skill.state.ghost.*;
import lombok.Getter;

@Getter
public class WisieAnswerGhostSkillFlow {
    private WisieAnswerFlow flow;
    private WisieAnswerManager manager;

    private boolean ghostUsed = false;
    private boolean ghostUsedOnIt = false;

    public WisieAnswerGhostSkillFlow(WisieAnswerFlow flow) {
        this.flow = flow;
        this.manager = flow.getManager();
    }

    private synchronized void phaseGhost(WisieAnswerManager opponent) {
        AbstractState prevState = flow.lastFlowableState();
        flow.addSkillState(new WisieStatePreparingDisguise(manager)).setPrevState(prevState).addOnFlowableEndListener(aLong1 -> {
            WisieStateTryingToScare ghostTryState = new WisieStateTryingToScare(manager, opponent);
            flow.addState(ghostTryState);
            boolean success = ghostTryState.calculateSuccess();
            ghostTryState.setSuccess(success);
            ghostTryState.addOnFlowableEndListener(aLong2 -> {
                if (success) {
                    flow.addState(new WisieStateScareSucceeded(manager, opponent)).addOnFlowableEndListener(aLong3 -> {
                        phaseGhostBan(prevState, true);
                    }).startFlowable();
                } else {
                    flow.addState(new WisieStateScareFailed(manager, opponent)).addOnFlowableEndListener(aLong3 -> {
                        phaseGhostBan(prevState, false);
                    }).startFlowable();
                }
            }).startFlowable();
        }).startFlowable();
    }

    private synchronized void phaseGhostBan(AbstractState prevState, boolean success) {
        flow.addState(new WisieStateRemovingDisguise(manager)).addOnFlowableEndListener(aLong3 -> {
            if (flow.addState(new WisieStateCheckWasCaught(manager, success)).startBoolean()) {
                flow.addState(new WisieStateWasCaught(manager)).addOnFlowableEndListener(aLong4 -> {
                    if(success){
                        manager.getWarManager().getFlow().ghostScaredAndCaught();
                    }
                }).startFlowable();
            } else {
                flow.addState(new WisieStateWasNotCaught(manager)).addOnFlowableEndListener(aLong4 -> {
                    prevState.startFlowableEndListeners();
                }).startFlowable();
            }
        }).startFlowable();
    }

    public void ghost(WisieAnswerManager opponent) {
        if (ghostUsed) {
            return;
        }
        flow.dispose();
        ghostUsed = true;
        phaseGhost(opponent);
    }

    private synchronized void phaseGhostUsedOnIt(boolean success, long interval) {
        AbstractState prevState = flow.lastFlowableState();
        flow.addState(new WisieStateScaringOnIt(manager, interval)).addOnFlowableEndListener(aLong1 -> {
            if (success) {
                flow.addState(new WisieStateRunAway(manager)).startVoid();
            } else {
                flow.addState(new WisieStateWasNotScared(manager)).addOnFlowableEndListener(aLong3 -> {
                    prevState.startFlowableEndListeners();
                }).startFlowable();
            }
        }).startFlowable();
    }

    public void ghostUsedOnIt(boolean success, long interval) {
        if (ghostUsedOnIt) {
            return;
        }
        flow.dispose();
        ghostUsedOnIt = true;
        phaseGhostUsedOnIt(success, interval);
    }

}
