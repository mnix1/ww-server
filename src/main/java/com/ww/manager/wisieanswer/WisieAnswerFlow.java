package com.ww.manager.wisieanswer;

import com.ww.manager.wisieanswer.state.State;
import com.ww.manager.wisieanswer.state.hint.*;
import com.ww.manager.wisieanswer.state.multiphase.StateCheckNoConcentration;
import com.ww.manager.wisieanswer.state.multiphase.StateLostConcentration;
import com.ww.manager.wisieanswer.state.phase1.StateStartRecognizingQuestion;
import com.ww.manager.wisieanswer.state.phase2.StateCheckKnowAnswerAfterThinkingAboutQuestion;
import com.ww.manager.wisieanswer.state.phase2.StateEndRecognizingQuestion;
import com.ww.manager.wisieanswer.state.phase2.StateStartThinkingAboutQuestion;
import com.ww.manager.wisieanswer.state.phase3.*;
import com.ww.manager.wisieanswer.state.phase4.StateEndRecognizingAnswers;
import com.ww.manager.wisieanswer.state.phase4.StateStartRecognizingAnswers;
import com.ww.manager.wisieanswer.state.phase5.StateAnsweringPhase5;
import com.ww.manager.wisieanswer.state.phase5.StateCheckKnowAnswerAfterThinkingWhichMatch;
import com.ww.manager.wisieanswer.state.phase5.StateEndThinkingWhichAnswerMatch;
import com.ww.manager.wisieanswer.state.phase5.StateThinkKnowAnswer;
import com.ww.manager.wisieanswer.state.phase6.*;
import com.ww.manager.wisieanswer.state.waterpistol.StateCleaning;
import com.ww.manager.wisieanswer.state.waterpistol.StateWaterPistolUsedOnIt;
import com.ww.model.constant.wisie.WisieAnswerAction;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
public class WisieAnswerFlow {
    protected static final Logger logger = LoggerFactory.getLogger(WisieAnswerFlow.class);

    private WisieAnswerManager manager;
    private State state;

    public WisieAnswerFlow(WisieAnswerManager manager) {
        this.manager = manager;
    }

    public void start() {
        phase1();
    }

    public void stop() {
        dispose();
    }

    private synchronized void dispose() {
        state.dispose();
    }

    private synchronized void phase1() {
        state = new StateStartRecognizingQuestion(manager).addOnFlowableEndListener(aLong1 -> {
            synchronized (this) {
                WisieAnswerAction aa1 = new StateCheckNoConcentration(manager).startWisieAnswerAction();
                if (WisieAnswerAction.isNoConcentration(aa1)) {
                    state = new StateLostConcentration(manager, aa1).addOnFlowableEndListener(aLong2 -> {
                        phase2();
                    }).startFlowable();
                } else {
                    phase2();
                }
            }
        }).startFlowable();
    }

    private synchronized void phase2() {
        state = new StateEndRecognizingQuestion(manager).addOnFlowableEndListener(aLong2 -> {
            state = new StateStartThinkingAboutQuestion(manager).addOnFlowableEndListener(aLong3 -> {
                synchronized (this) {
                    WisieAnswerAction aa2 = new StateCheckKnowAnswerAfterThinkingAboutQuestion(manager).startWisieAnswerAction();
                    if (aa2 == WisieAnswerAction.THINK_KNOW_ANSWER) {
                        phase3();
                    } else if (aa2 == WisieAnswerAction.NOT_SURE_OF_ANSWER) {
                        phase4();
                    }
                }
            }).startFlowable();
        }).startFlowable();
    }

    private synchronized void phase3() {
        state = new StateStartLookingForAnswer(manager).addOnFlowableEndListener(aLong4 -> {
            state = new StateEndLookingForAnswer(manager).addOnFlowableEndListener(aLong5 -> {
                synchronized (this) {
                    WisieAnswerAction aa2 = new StateCheckFoundAnswerLookingFor(manager).startWisieAnswerAction();
                    if (aa2 == WisieAnswerAction.FOUND_ANSWER_LOOKING_FOR) {
                        state = new StateFoundAnswerLookingFor(manager).addOnFlowableEndListener(aLong6 -> {
                            new StateAnsweringPhase3(manager).startVoid();
                        }).startFlowable();
                    } else if (aa2 == WisieAnswerAction.NO_FOUND_ANSWER_LOOKING_FOR) {
                        state = new StateNoFoundAnswerLookingFor(manager).addOnFlowableEndListener(aLong7 -> {
                            phase5();
                        }).startFlowable();
                    }
                }
            }).startFlowable();
        }).startFlowable();
    }

    private synchronized void phase4() {
        state = new StateStartRecognizingAnswers(manager).addOnFlowableEndListener(aLong5 -> {
            state = new StateEndRecognizingAnswers(manager).addOnFlowableEndListener(aLong6 -> {
                synchronized (this) {
                    WisieAnswerAction aa3 = new StateCheckNoConcentration(manager).startWisieAnswerAction();
                    if (WisieAnswerAction.isNoConcentration(aa3)) {
                        state = new StateLostConcentration(manager, aa3).addOnFlowableEndListener(aLong7 -> {
                            phase5();
                        }).startFlowable();
                    } else {
                        phase5();
                    }
                }
            }).startFlowable();
        }).startFlowable();
    }

    private synchronized void phase5() {
        state = new StateEndThinkingWhichAnswerMatch(manager).addOnFlowableEndListener(aLong8 -> {
            synchronized (this) {
                WisieAnswerAction aa4 = new StateCheckKnowAnswerAfterThinkingWhichMatch(manager).startWisieAnswerAction();
                if (aa4 == WisieAnswerAction.THINK_KNOW_ANSWER) {
                    state = new StateThinkKnowAnswer(manager).addOnFlowableEndListener(aLong9 -> {
                        new StateAnsweringPhase5(manager).startVoid();
                    }).startFlowable();
                } else if (aa4 == WisieAnswerAction.NOT_SURE_OF_ANSWER) {
                    phase6();
                }
            }
        }).startFlowable();
    }

    private synchronized void phase6() {
        state = new StateNotSureOfAnswer(manager).addOnFlowableEndListener(aLong10 -> {
            state = new StateEndThinkingIfGiveRandomAnswer(manager).addOnFlowableEndListener(aLong11 -> {
                synchronized (this) {
                    WisieAnswerAction aa5 = new StateCheckIfGiveRandomAnswer(manager).startWisieAnswerAction();
                    if (aa5 == WisieAnswerAction.WILL_GIVE_RANDOM_ANSWER) {
                        new StateAnsweringPhase6(manager).startVoid();
                    } else if (aa5 == WisieAnswerAction.WONT_GIVE_RANDOM_ANSWER) {
                        new StateSurrender(manager).startVoid();
                    }
                }
            }).startFlowable();
        }).startFlowable();
    }

    private synchronized void phaseHint() {
        state = new StateHintReceived(manager).addOnFlowableEndListener(aLong10 -> {
            state = new StateEndThinkingIfUseHint(manager).addOnFlowableEndListener(aLong11 -> {
                synchronized (this) {
                    WisieAnswerAction aa5 = new StateCheckIfUseHint(manager).startWisieAnswerAction();
                    state = new StateDecidedIfUseHint(manager, aa5).addOnFlowableEndListener(aLong12 -> {
                        synchronized (this) {
                            if (aa5 == WisieAnswerAction.WILL_USE_HINT) {
                                new StateAnsweringUseHint(manager).startVoid();
                            } else if (aa5 == WisieAnswerAction.WONT_USE_HINT) {
                                new StateAnsweringNoUseHint(manager).startVoid();
                            }
                        }
                    }).startFlowable();
                }
            }).startFlowable();
        }).startFlowable();
    }

    private synchronized void phaseWaterPistol() {
        State prevState = state;
        state = new StateWaterPistolUsedOnIt(manager).addOnFlowableEndListener(aLong1 -> {
            state = new StateCleaning(manager).addOnFlowableEndListener(aLong2 -> {
                prevState.startFlowable();
            }).startFlowable();
        }).startFlowable();
    }

    public void hint(Long answerId, Boolean isCorrect) {
        if (manager.isReceivedHint()) {
            return;
        }
        dispose();
        manager.setReceivedHint(true);
        manager.setHintAnswerId(answerId);
        manager.setHintCorrect(isCorrect);
        phaseHint();
    }

    public void waterPistol() {
        if (manager.isWaterPistolUsedOnIt()) {
            return;
        }
        dispose();
        manager.setWaterPistolUsedOnIt(true);
        phaseWaterPistol();
    }

}
