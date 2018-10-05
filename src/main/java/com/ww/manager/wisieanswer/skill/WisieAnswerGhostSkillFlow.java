package com.ww.manager.wisieanswer.skill;

import com.ww.manager.AbstractState;
import com.ww.manager.wisieanswer.WisieAnswerFlow;
import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.skill.state.ghost.*;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
public class WisieAnswerGhostSkillFlow {
    protected static final Logger logger = LoggerFactory.getLogger(WisieAnswerGhostSkillFlow.class);

    private WisieAnswerFlow flow;
    private WisieAnswerManager manager;

    private boolean ghostUsed = false;
    private boolean ghostUsedOnIt = false;

    public WisieAnswerGhostSkillFlow(WisieAnswerFlow flow) {
        this.flow = flow;
        this.manager = flow.getManager();
    }

    private synchronized void phaseGhost(WisieAnswerManager opponent) {
        AbstractState prevState = flow.getState();
        flow.setState(new WisieStatePreparingDisguise(manager).addOnFlowableEndListener(aLong1 -> {
            synchronized (this) {
                WisieStateTryingToScare ghostTryState = new WisieStateTryingToScare(manager, opponent);
                boolean success = ghostTryState.calculateSuccess();
                long interval = ghostTryState.calculateInterval();
                ghostTryState.setSuccess(success);
                ghostTryState.setInterval(interval);
                flow.setState(ghostTryState.addOnFlowableEndListener(aLong2 -> {
                    if (success) {
                        flow.setState(new WisieStateScareSucceeded(manager, opponent).addOnFlowableEndListener(aLong3 -> {
                            phaseGhostBan(prevState, true);
                        }).startFlowable());
                    } else {
                        flow.setState(new WisieStateScareFailed(manager, opponent).addOnFlowableEndListener(aLong3 -> {
                            phaseGhostBan(prevState, false);
                        }).startFlowable());
                    }
                }).startFlowable());
            }
        }).startFlowable());
    }

    private synchronized void phaseGhostBan(AbstractState prevState, boolean success) {
        flow.setState(new WisieStateRemovingDisguise(manager).addOnFlowableEndListener(aLong3 -> {
            if (new WisieStateCheckWasCaught(manager, success).startBoolean()) {
                new WisieStateWasCaught(manager).startVoid();
            } else {
                flow.setState(new WisieStateWasNotCaught(manager).addOnFlowableEndListener(aLong4 -> {
                    prevState.startFlowableEndListeners();
                }).startFlowable());
            }
        }).startFlowable());
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
        AbstractState prevState = flow.getState();
        flow.setState(new WisieStateScaringOnIt(manager, interval).addOnFlowableEndListener(aLong1 -> {
            if (success) {
                new WisieStateRunAway(manager).startVoid();
            } else {
                flow.setState(new WisieStateWasNotScared(manager).addOnFlowableEndListener(aLong3 -> {
                    prevState.startFlowableEndListeners();
                }).startFlowable());
            }
        }).startFlowable());
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
