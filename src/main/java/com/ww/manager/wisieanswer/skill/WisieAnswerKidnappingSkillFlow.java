package com.ww.manager.wisieanswer.skill;

import com.ww.manager.AbstractState;
import com.ww.manager.wisieanswer.WisieAnswerFlow;
import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.skill.state.kidnapping.*;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
public class WisieAnswerKidnappingSkillFlow {
    protected static final Logger logger = LoggerFactory.getLogger(WisieAnswerKidnappingSkillFlow.class);

    private WisieAnswerFlow flow;
    private WisieAnswerManager manager;

    private boolean kidnappingUsed = false;
    private boolean kidnappingUsedOnIt = false;

    public WisieAnswerKidnappingSkillFlow(WisieAnswerFlow flow) {
        this.flow = flow;
        this.manager = flow.getManager();
    }

    private synchronized void phaseKidnapping(WisieAnswerManager opponent) {
        AbstractState prevState = flow.getState();
        flow.setState(new WisieStatePreparingKidnapping(manager).addOnFlowableEndListener(aLong1 -> {
            synchronized (this) {
                WisieStateTryingToKidnap kidnapTryState = new WisieStateTryingToKidnap(manager, opponent);
                boolean success = kidnapTryState.calculateSuccess();
                long interval = kidnapTryState.calculateInterval();
                kidnapTryState.setSuccess(success);
                kidnapTryState.setInterval(interval);
                flow.setState(kidnapTryState.addOnFlowableEndListener(aLong2 -> {
                    if (success) {
                        flow.setState(new WisieStateKidnappingSucceeded(manager, opponent).addOnFlowableEndListener(aLong3 -> {
                            manager.getManager().getFlow().kidnapped();
                        }).startFlowable());
                    } else {
                        flow.setState(new WisieStateKidnappingFailed(manager, opponent).addOnFlowableEndListener(aLong3 -> {
                            flow.setState(new WisieStateChangingClothes(manager).addOnFlowableEndListener(aLong4 -> {
                                new WisieStateContinueAfterKidnapping(manager).startVoid();
                                flow.setState(prevState.startFlowable());
                            }).startFlowable());
                        }).startFlowable());
                    }
                }).startFlowable());
            }
        }).startFlowable());
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
        AbstractState prevState = flow.getState();
        flow.setState(new WisieStateTryingToDefend(manager, interval).addOnFlowableEndListener(aLong1 -> {
            if (success) {
                new WisieStateWasKidnapped(manager).startVoid();
            } else {
                flow.setState(new WisieStateWasNotKidnapped(manager).addOnFlowableEndListener(aLong3 -> {
                    flow.setState(prevState.startFlowable());
                }).startFlowable());
            }
        }).startFlowable());
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
