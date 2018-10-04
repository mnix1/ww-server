package com.ww.manager.wisieanswer;

import com.ww.manager.rival.state.AbstractState;
import com.ww.manager.wisieanswer.state.phase2.WisieStateStartThinkingAboutQuestion;
import com.ww.manager.wisieanswer.state.skill.hint.*;
import com.ww.manager.wisieanswer.state.skill.kidnapping.*;
import com.ww.manager.wisieanswer.state.skill.waterpistol.WisieStateCleaning;
import com.ww.manager.wisieanswer.state.skill.waterpistol.WisieStateWaterPistolUsedOnIt;
import com.ww.model.constant.wisie.WisieAnswerAction;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
public class WisieAnswerSkillFlow {
    protected static final Logger logger = LoggerFactory.getLogger(WisieAnswerSkillFlow.class);

    private WisieAnswerFlow flow;
    private WisieAnswerManager manager;

    private boolean receivedHint = false;
    private Long hintAnswerId;
    private boolean hintCorrect;

    private boolean waterPistolUsedOnIt = false;

    private boolean kidnappingUsed = false;
    private boolean kidnappingUsedOnIt = false;

    public WisieAnswerSkillFlow(WisieAnswerFlow flow) {
        this.flow = flow;
        this.manager = flow.getManager();
    }

    private synchronized void phaseHint() {
        flow.setState(new WisieStateHintReceived(manager).addOnFlowableEndListener(aLong10 -> {
            flow.setState(new WisieStateEndThinkingIfUseHint(manager, hintCorrect).addOnFlowableEndListener(aLong11 -> {
                synchronized (this) {
                    WisieAnswerAction aa5 = new WisieStateCheckIfUseHint(manager, hintCorrect).startWisieAnswerAction();
                    flow.setState(new WisieStateDecidedIfUseHint(manager, aa5).addOnFlowableEndListener(aLong12 -> {
                        synchronized (this) {
                            if (aa5 == WisieAnswerAction.WILL_USE_HINT) {
                                new WisieStateAnsweringUseHint(manager, hintCorrect, hintAnswerId).startVoid();
                            } else if (aa5 == WisieAnswerAction.WONT_USE_HINT) {
                                flow.setState(new WisieStateStartThinkingAboutQuestion(manager).addOnFlowableEndListener(aLong3 -> {
                                    new WisieStateAnsweringNoUseHint(manager, hintAnswerId).startVoid();
                                }).startFlowable());
                            }
                        }
                    }).startFlowable());
                }
            }).startFlowable());
        }).startFlowable());
    }

    public void hint(Long answerId, Boolean isCorrect) {
        if (receivedHint) {
            return;
        }
        flow.dispose();
        receivedHint = true;
        hintAnswerId = answerId;
        hintCorrect = isCorrect;
        phaseHint();
    }

    private synchronized void phaseWaterPistol() {
        AbstractState prevState = flow.getState();
        flow.setState(new WisieStateWaterPistolUsedOnIt(manager).addOnFlowableEndListener(aLong1 -> {
            flow.setState(new WisieStateCleaning(manager).addOnFlowableEndListener(aLong2 -> {
                flow.setState(prevState.startFlowable());
            }).startFlowable());
        }).startFlowable());
    }

    public void waterPistol() {
        if (waterPistolUsedOnIt) {
            return;
        }
        flow.dispose();
        waterPistolUsedOnIt = true;
        phaseWaterPistol();
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
                        flow.setState(new WisieStateKidnappingSucceeded(manager).addOnFlowableEndListener(aLong3 -> {
                            manager.getManager().getFlow().kidnapped();
                        }).startFlowable());
                    } else {
                        flow.setState(new WisieStateKidnappingFailed(manager).addOnFlowableEndListener(aLong3 -> {
                            flow.setState(prevState.startFlowable());
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
