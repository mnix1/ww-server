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

    public WisieAnswerSkillFlow(WisieAnswerFlow flow) {
        this.flow = flow;
    }

    private synchronized void phaseHint() {
        flow.setState(new WisieStateHintReceived(flow.getManager()).addOnFlowableEndListener(aLong10 -> {
            flow.setState(new WisieStateEndThinkingIfUseHint(flow.getManager()).addOnFlowableEndListener(aLong11 -> {
                synchronized (this) {
                    WisieAnswerAction aa5 = new WisieStateCheckIfUseHint(flow.getManager()).startWisieAnswerAction();
                    flow.setState(new WisieStateDecidedIfUseHint(flow.getManager(), aa5).addOnFlowableEndListener(aLong12 -> {
                        synchronized (this) {
                            if (aa5 == WisieAnswerAction.WILL_USE_HINT) {
                                new WisieStateAnsweringUseHint(flow.getManager()).startVoid();
                            } else if (aa5 == WisieAnswerAction.WONT_USE_HINT) {
                                flow.setState(new WisieStateStartThinkingAboutQuestion(flow.getManager()).addOnFlowableEndListener(aLong3 -> {
                                    new WisieStateAnsweringNoUseHint(flow.getManager()).startVoid();
                                }).startFlowable());
                            }
                        }
                    }).startFlowable());
                }
            }).startFlowable());
        }).startFlowable());
    }

    public void hint(Long answerId, Boolean isCorrect) {
        if (flow.getManager().isReceivedHint()) {
            return;
        }
        flow.dispose();
        flow.getManager().setReceivedHint(true);
        flow.getManager().setHintAnswerId(answerId);
        flow.getManager().setHintCorrect(isCorrect);
        phaseHint();
    }

    private synchronized void phaseWaterPistol() {
        AbstractState prevState = flow.getState();
        flow.setState(new WisieStateWaterPistolUsedOnIt(flow.getManager()).addOnFlowableEndListener(aLong1 -> {
            flow.setState(new WisieStateCleaning(flow.getManager()).addOnFlowableEndListener(aLong2 -> {
                prevState.startFlowable();
            }).startFlowable());
        }).startFlowable());
    }

    public void waterPistol() {
        if (flow.getManager().isWaterPistolUsedOnIt()) {
            return;
        }
        flow.dispose();
        flow.getManager().setWaterPistolUsedOnIt(true);
        phaseWaterPistol();
    }

    private synchronized void phaseKidnapping(WisieAnswerManager opponent) {
        AbstractState prevState = flow.getState();
        flow.setState(new WisieStatePreparingKidnapping(flow.getManager()).addOnFlowableEndListener(aLong1 -> {
            synchronized (this) {
                WisieStateTryingToKidnap kidnapTryState = new WisieStateTryingToKidnap(flow.getManager(), opponent);
                boolean success = kidnapTryState.calculateSuccess();
                long interval = kidnapTryState.calculateInterval();
                kidnapTryState.setSuccess(success);
                kidnapTryState.setInterval(interval);
                flow.setState(kidnapTryState.addOnFlowableEndListener(aLong2 -> {
                    if (success) {
                        new WisieStateKidnappingSucceeded(flow.getManager()).startVoid();
                    } else {
                        flow.setState(new WisieStateKidnappingFailed(flow.getManager()).addOnFlowableEndListener(aLong3 -> {
                            prevState.startFlowable();
                        }).startFlowable());
                    }
                    prevState.startFlowable();
                }).startFlowable());
            }
        }).startFlowable());
    }

    public void kidnapping(WisieAnswerManager opponent) {
        if (flow.getManager().isKidnappingUsed()) {
            return;
        }
        flow.dispose();
        flow.getManager().setKidnappingUsed(true);
        phaseKidnapping(opponent);
    }

    private synchronized void phaseKidnappingUsedOnIt(boolean success, long interval) {
        AbstractState prevState = flow.getState();
        flow.setState(new WisieStateTryingToDefend(flow.getManager(), interval).addOnFlowableEndListener(aLong1 -> {
            if (success) {
                new WisieStateWasKidnapped(flow.getManager()).startVoid();
            } else {
                flow.setState(new WisieStateWasNotKidnapped(flow.getManager()).addOnFlowableEndListener(aLong3 -> {
                    prevState.startFlowable();
                }).startFlowable());
            }
        }).startFlowable());
    }

    public void kidnappingUsedOnIt(boolean success, long interval) {
        if (flow.getManager().isKidnappingUsedOnIt()) {
            return;
        }
        flow.dispose();
        flow.getManager().setKidnappingUsedOnIt(true);
        phaseKidnappingUsedOnIt(success, interval);
    }

}
