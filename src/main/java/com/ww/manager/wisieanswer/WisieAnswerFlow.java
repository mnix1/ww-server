package com.ww.manager.wisieanswer;

import com.ww.manager.rival.state.AbstractState;
import com.ww.manager.wisieanswer.skill.WisieAnswerGhostSkillFlow;
import com.ww.manager.wisieanswer.skill.WisieAnswerHintSkillFlow;
import com.ww.manager.wisieanswer.skill.WisieAnswerKidnappingSkillFlow;
import com.ww.manager.wisieanswer.skill.WisieAnswerWaterPistolSkillFlow;
import com.ww.manager.wisieanswer.state.multiphase.WisieStateCheckNoConcentration;
import com.ww.manager.wisieanswer.state.multiphase.WisieStateLostConcentration;
import com.ww.manager.wisieanswer.state.phase1.WisieStateStartRecognizingQuestion;
import com.ww.manager.wisieanswer.state.phase2.WisieStateCheckKnowAnswerAfterThinkingAboutQuestion;
import com.ww.manager.wisieanswer.state.phase2.WisieStateEndRecognizingQuestion;
import com.ww.manager.wisieanswer.state.phase2.WisieStateStartThinkingAboutQuestion;
import com.ww.manager.wisieanswer.state.phase3.*;
import com.ww.manager.wisieanswer.state.phase4.WisieStateEndRecognizingAnswers;
import com.ww.manager.wisieanswer.state.phase4.WisieStateStartRecognizingAnswers;
import com.ww.manager.wisieanswer.state.phase5.*;
import com.ww.manager.wisieanswer.state.phase5.WisieStateCheckKnowAnswerAfterThinkingWhichMatch;
import com.ww.manager.wisieanswer.state.phase5.WisieStateEndThinkingWhichAnswerMatch;
import com.ww.manager.wisieanswer.state.phase6.*;
import com.ww.model.constant.wisie.WisieAnswerAction;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
public class WisieAnswerFlow {
    protected static final Logger logger = LoggerFactory.getLogger(WisieAnswerFlow.class);

    private WisieAnswerManager manager;
    private AbstractState state;

    private WisieAnswerHintSkillFlow hintSkillFlow;
    private WisieAnswerWaterPistolSkillFlow waterPistolSkillFlow;
    private WisieAnswerKidnappingSkillFlow kidnappingSkillFlow;
    private WisieAnswerGhostSkillFlow ghostSkillFlow;

    public WisieAnswerFlow(WisieAnswerManager manager) {
        this.manager = manager;
        this.hintSkillFlow = new WisieAnswerHintSkillFlow(this);
        this.kidnappingSkillFlow = new WisieAnswerKidnappingSkillFlow(this);
        this.waterPistolSkillFlow = new WisieAnswerWaterPistolSkillFlow(this);
        this.ghostSkillFlow = new WisieAnswerGhostSkillFlow(this);
    }

    public void start() {
        manager.setRunning(true);
        phase1();
    }

    public void stop() {
        manager.setRunning(false);
        dispose();
    }

    public void setState(AbstractState state) {
        this.state = state;
    }

    public synchronized void dispose() {
        state.dispose();
    }

    private synchronized void phase1() {
        state = new WisieStateStartRecognizingQuestion(manager).addOnFlowableEndListener(aLong1 -> {
            synchronized (this) {
                WisieAnswerAction aa1 = new WisieStateCheckNoConcentration(manager).startWisieAnswerAction();
                if (WisieAnswerAction.isNoConcentration(aa1)) {
                    state = new WisieStateLostConcentration(manager, aa1).addOnFlowableEndListener(aLong2 -> {
                        phase2();
                    }).startFlowable();
                } else {
                    phase2();
                }
            }
        }).startFlowable();
    }

    private synchronized void phase2() {
        state = new WisieStateEndRecognizingQuestion(manager).addOnFlowableEndListener(aLong2 -> {
            state = new WisieStateStartThinkingAboutQuestion(manager).addOnFlowableEndListener(aLong3 -> {
                synchronized (this) {
                    WisieAnswerAction aa2 = new WisieStateCheckKnowAnswerAfterThinkingAboutQuestion(manager).startWisieAnswerAction();
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
        state = new WisieStateStartLookingForAnswer(manager).addOnFlowableEndListener(aLong4 -> {
            state = new WisieStateEndLookingForAnswer(manager).addOnFlowableEndListener(aLong5 -> {
                synchronized (this) {
                    WisieAnswerAction aa2 = new WisieStateCheckFoundAnswerLookingFor(manager).startWisieAnswerAction();
                    if (aa2 == WisieAnswerAction.FOUND_ANSWER_LOOKING_FOR) {
                        state = new WisieStateFoundAnswerLookingFor(manager).addOnFlowableEndListener(aLong6 -> {
                            new WisieStateAnsweringPhase3(manager).startVoid();
                        }).startFlowable();
                    } else if (aa2 == WisieAnswerAction.NO_FOUND_ANSWER_LOOKING_FOR) {
                        state = new WisieStateNoFoundAnswerLookingFor(manager).addOnFlowableEndListener(aLong7 -> {
                            phase5();
                        }).startFlowable();
                    }
                }
            }).startFlowable();
        }).startFlowable();
    }

    private synchronized void phase4() {
        state = new WisieStateStartRecognizingAnswers(manager).addOnFlowableEndListener(aLong5 -> {
            state = new WisieStateEndRecognizingAnswers(manager).addOnFlowableEndListener(aLong6 -> {
                synchronized (this) {
                    WisieAnswerAction aa3 = new WisieStateCheckNoConcentration(manager).startWisieAnswerAction();
                    if (WisieAnswerAction.isNoConcentration(aa3)) {
                        state = new WisieStateLostConcentration(manager, aa3).addOnFlowableEndListener(aLong7 -> {
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
        state = new WisieStateEndThinkingWhichAnswerMatch(manager).addOnFlowableEndListener(aLong8 -> {
            synchronized (this) {
                WisieAnswerAction aa4 = new WisieStateCheckKnowAnswerAfterThinkingWhichMatch(manager).startWisieAnswerAction();
                if (aa4 == WisieAnswerAction.NOW_KNOW_ANSWER) {
                    state = new WisieStateNowKnowAnswer(manager).addOnFlowableEndListener(aLong9 -> {
                        new WisieStateAnsweringPhase5(manager).startVoid();
                    }).startFlowable();
                } else if (aa4 == WisieAnswerAction.DOESNT_KNOW_ANSWER) {
                    phase6();
                }
            }
        }).startFlowable();
    }

    private synchronized void phase6() {
        state = new WisieStateNotSureOfAnswer(manager).addOnFlowableEndListener(aLong10 -> {
            state = new WisieStateEndThinkingIfGiveRandomAnswer(manager).addOnFlowableEndListener(aLong11 -> {
                synchronized (this) {
                    WisieAnswerAction aa5 = new WisieStateCheckIfGiveRandomAnswer(manager).startWisieAnswerAction();
                    if (aa5 == WisieAnswerAction.WILL_GIVE_RANDOM_ANSWER) {
                        new WisieStateAnsweringPhase6(manager).startVoid();
                    } else if (aa5 == WisieAnswerAction.WONT_GIVE_RANDOM_ANSWER) {
                        new WisieStateSurrender(manager).startVoid();
                    }
                }
            }).startFlowable();
        }).startFlowable();
    }

}
