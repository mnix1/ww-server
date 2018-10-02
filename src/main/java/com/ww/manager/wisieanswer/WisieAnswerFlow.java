package com.ww.manager.wisieanswer;

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
import com.ww.model.constant.wisie.WisieAnswerAction;
import io.reactivex.disposables.Disposable;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
public class WisieAnswerFlow {
    protected static final Logger logger = LoggerFactory.getLogger(WisieAnswerFlow.class);

    private WisieAnswerManager manager;

    private Disposable activeFlowable;

    public WisieAnswerFlow(WisieAnswerManager manager) {
        this.manager = manager;
    }


    public void disposeFlowable() {
        if (activeFlowable != null) {
            activeFlowable.dispose();
            activeFlowable = null;
        }
    }

    public void start() {
        manager.setInProgress(true);
        phase1();
    }

    public void stop() {
        if (manager.isInProgress()) {
            manager.setInProgress(false);
            disposeFlowable();
        }
    }

    public void phase1() {
        activeFlowable = new StateStartRecognizingQuestion(manager).startFlowable().subscribe(aLong1 -> {
            WisieAnswerAction aa1 = new StateCheckNoConcentration(manager).startWisieAnswerAction();
            if (WisieAnswerAction.isNoConcentration(aa1)) {
                activeFlowable = new StateLostConcentration(manager, aa1).startFlowable().subscribe(aLong2 -> {
                    phase2();
                });
            } else {
                phase2();
            }
        });
    }

    public void phase2() {
        activeFlowable = new StateEndRecognizingQuestion(manager).startFlowable().subscribe(aLong2 -> {
            activeFlowable = new StateStartThinkingAboutQuestion(manager).startFlowable().subscribe(aLong3 -> {
                WisieAnswerAction aa2 = new StateCheckKnowAnswerAfterThinkingAboutQuestion(manager).startWisieAnswerAction();
                if (aa2 == WisieAnswerAction.THINK_KNOW_ANSWER) {
                    phase3();
                } else if (aa2 == WisieAnswerAction.NOT_SURE_OF_ANSWER) {
                    phase4();
                }
            });
        });
    }

    public void phase3() {
        activeFlowable = new StateStartLookingForAnswer(manager).startFlowable().subscribe(aLong4 -> {
            activeFlowable = new StateEndLookingForAnswer(manager).startFlowable().subscribe(aLong5 -> {
                WisieAnswerAction aa2 = new StateCheckFoundAnswerLookingFor(manager).startWisieAnswerAction();
                if (aa2 == WisieAnswerAction.FOUND_ANSWER_LOOKING_FOR) {
                    activeFlowable = new StateFoundAnswerLookingFor(manager).startFlowable().subscribe(aLong6 -> {
                        new StateAnsweringPhase3(manager).startVoid();
                    });
                } else if (aa2 == WisieAnswerAction.NO_FOUND_ANSWER_LOOKING_FOR) {
                    activeFlowable = new StateNoFoundAnswerLookingFor(manager).startFlowable().subscribe(aLong7 -> {
                        phase5();
                    });
                }
            });
        });
    }

    public void phase4() {
        activeFlowable = new StateStartRecognizingAnswers(manager).startFlowable().subscribe(aLong5 -> {
            activeFlowable = new StateEndRecognizingAnswers(manager).startFlowable().subscribe(aLong6 -> {
                WisieAnswerAction aa3 = new StateCheckNoConcentration(manager).startWisieAnswerAction();
                if (WisieAnswerAction.isNoConcentration(aa3)) {
                    new StateLostConcentration(manager, aa3).startFlowable().subscribe(aLong7 -> {
                        phase5();
                    });
                } else {
                    phase5();
                }
            });
        });
    }

    public void phase5() {
        activeFlowable = new StateEndThinkingWhichAnswerMatch(manager).startFlowable().subscribe(aLong8 -> {
            WisieAnswerAction aa4 = new StateCheckKnowAnswerAfterThinkingWhichMatch(manager).startWisieAnswerAction();
            if (aa4 == WisieAnswerAction.THINK_KNOW_ANSWER) {
                activeFlowable = new StateThinkKnowAnswer(manager).startFlowable().subscribe(aLong9 -> {
                    new StateAnsweringPhase5(manager).startVoid();
                });
            } else if (aa4 == WisieAnswerAction.NOT_SURE_OF_ANSWER) {
                phase6();
            }
        });
    }

    public void phase6() {
        activeFlowable = new StateNotSureOfAnswer(manager).startFlowable().subscribe(aLong10 -> {
            activeFlowable = new StateEndThinkingIfGiveRandomAnswer(manager).startFlowable().subscribe(aLong11 -> {
                WisieAnswerAction aa5 = new StateCheckIfGiveRandomAnswer(manager).startWisieAnswerAction();
                if (aa5 == WisieAnswerAction.WILL_GIVE_RANDOM_ANSWER) {
                    new StateAnsweringPhase6(manager).startVoid();
                } else if (aa5 == WisieAnswerAction.WONT_GIVE_RANDOM_ANSWER) {
                    new StateSurrender(manager).startVoid();
                }
            });
        });
    }

    public void phaseHint() {
        activeFlowable = new StateHintReceived(manager).startFlowable().subscribe(aLong10 -> {
            activeFlowable = new StateEndThinkingIfUseHint(manager).startFlowable().subscribe(aLong11 -> {
                WisieAnswerAction aa5 = new StateCheckIfUseHint(manager).startWisieAnswerAction();
                activeFlowable = new StateDecidedIfUseHint(manager, aa5).startFlowable().subscribe(aLong12 -> {
                    if (aa5 == WisieAnswerAction.WILL_USE_HINT) {
                        new StateAnsweringUseHint(manager).startVoid();
                    } else if (aa5 == WisieAnswerAction.WONT_USE_HINT) {
                        new StateAnsweringNoUseHint(manager).startVoid();
                    }
                });
            });
        });
    }

    public void hint(Long answerId, Boolean isCorrect) {
        if (manager.isReceivedHint() || !manager.isInProgress()) {
            return;
        }
        disposeFlowable();
        manager.setReceivedHint(true);
        manager.setHintAnswerId(answerId);
        manager.setHintCorrect(isCorrect);
        phaseHint();
    }

    public void waterPistol() {
        if (manager.isWaterPistolUsedOnIt() || !manager.isInProgress()) {
            return;
        }
        disposeFlowable();
        manager.setWaterPistolUsedOnIt(true);
    }

}
