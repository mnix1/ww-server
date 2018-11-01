package com.ww.manager.wisieanswer;

import com.ww.manager.AbstractFlow;
import com.ww.manager.wisieanswer.skill.*;
import com.ww.manager.wisieanswer.skill.state.WisieSkillState;
import com.ww.manager.wisieanswer.state.WisieState;
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
import com.ww.model.constant.wisie.MemberWisieStatus;
import lombok.Getter;

@Getter
public class WisieAnswerFlow extends AbstractFlow {
    private WisieAnswerManager manager;

    private WisieAnswerHintSkillFlow hintSkillFlow;
    private WisieAnswerWaterPistolSkillFlow waterPistolSkillFlow;
    private WisieAnswerKidnappingSkillFlow kidnappingSkillFlow;
    private WisieAnswerGhostSkillFlow ghostSkillFlow;
    private WisieAnswerPizzaSkillFlow pizzaSkillFlow;
    private WisieAnswerCoverallSkillFlow coverallSkillFlow;
    private WisieAnswerChangeTaskSkillFlow changeTaskSkillFlow;

    public WisieAnswerFlow(WisieAnswerManager manager) {
        this.manager = manager;
        this.hintSkillFlow = new WisieAnswerHintSkillFlow(this);
        this.kidnappingSkillFlow = new WisieAnswerKidnappingSkillFlow(this);
        this.waterPistolSkillFlow = new WisieAnswerWaterPistolSkillFlow(this);
        this.ghostSkillFlow = new WisieAnswerGhostSkillFlow(this);
        this.pizzaSkillFlow = new WisieAnswerPizzaSkillFlow(this);
        this.coverallSkillFlow = new WisieAnswerCoverallSkillFlow(this);
        this.changeTaskSkillFlow = new WisieAnswerChangeTaskSkillFlow(this);
    }

    public void start() {
        if (!manager.isRunning()) {
            manager.setRunning(true);
            phase1();
        }
    }

    public void stop() {
        if (manager.isRunning()) {
            manager.setRunning(false);
            dispose();
            logger.trace("WisieAnswerFlow stop, " + manager.describe() + statesDescribe());
        }
    }

    public synchronized WisieState addState(WisieState state) {
        super.addState(state);
        return state;
    }

    public synchronized WisieSkillState addSkillState(WisieSkillState state) {
        super.addState(state);
        return state;
    }

    private synchronized void phase1() {
        addState(new WisieStateStartRecognizingQuestion(manager)).addOnFlowableEndListener(aLong1 -> {
            MemberWisieStatus aa1 = addState(new WisieStateCheckNoConcentration(manager)).startWisieAnswerAction();
            if (MemberWisieStatus.isNoConcentration(aa1)) {
                addState(new WisieStateLostConcentration(manager, aa1)).addOnFlowableEndListener(aLong2 -> {
                    phase2();
                }).startFlowable();
            } else {
                phase2();
            }
        }).startFlowable();
    }

    private synchronized void phase2() {
        addState(new WisieStateEndRecognizingQuestion(manager)).addOnFlowableEndListener(aLong2 -> {
            addState(new WisieStateStartThinkingAboutQuestion(manager)).addOnFlowableEndListener(aLong3 -> {
                MemberWisieStatus aa2 = new WisieStateCheckKnowAnswerAfterThinkingAboutQuestion(manager).startWisieAnswerAction();
                if (aa2 == MemberWisieStatus.THINK_KNOW_ANSWER) {
                    phase3();
                } else if (aa2 == MemberWisieStatus.NOT_SURE_OF_ANSWER) {
                    phase4();
                }
            }).startFlowable();
        }).startFlowable();
    }

    private synchronized void phase3() {
        addState(new WisieStateStartLookingForAnswer(manager)).addOnFlowableEndListener(aLong4 -> {
            addState(new WisieStateEndLookingForAnswer(manager)).addOnFlowableEndListener(aLong5 -> {
                MemberWisieStatus aa2 = addState(new WisieStateCheckFoundAnswerLookingFor(manager)).startWisieAnswerAction();
                if (aa2 == MemberWisieStatus.FOUND_ANSWER_LOOKING_FOR) {
                    addState(new WisieStateFoundAnswerLookingFor(manager)).addOnFlowableEndListener(aLong6 -> {
                        addState(new WisieStateAnsweringPhase3(manager)).startVoid();
                    }).startFlowable();
                } else if (aa2 == MemberWisieStatus.NO_FOUND_ANSWER_LOOKING_FOR) {
                    addState(new WisieStateNoFoundAnswerLookingFor(manager)).addOnFlowableEndListener(aLong7 -> {
                        phase5();
                    }).startFlowable();
                }
            }).startFlowable();
        }).startFlowable();
    }

    private synchronized void phase4() {
        addState(new WisieStateStartRecognizingAnswers(manager)).addOnFlowableEndListener(aLong5 -> {
            addState(new WisieStateEndRecognizingAnswers(manager)).addOnFlowableEndListener(aLong6 -> {
                MemberWisieStatus aa3 = new WisieStateCheckNoConcentration(manager).startWisieAnswerAction();
                if (MemberWisieStatus.isNoConcentration(aa3)) {
                    addState(new WisieStateLostConcentration(manager, aa3)).addOnFlowableEndListener(aLong7 -> {
                        phase5();
                    }).startFlowable();
                } else {
                    phase5();
                }
            }).startFlowable();
        }).startFlowable();
    }

    private synchronized void phase5() {
        addState(new WisieStateEndThinkingWhichAnswerMatch(manager)).addOnFlowableEndListener(aLong8 -> {
            MemberWisieStatus aa4 = addState(new WisieStateCheckKnowAnswerAfterThinkingWhichMatch(manager)).startWisieAnswerAction();
            if (aa4 == MemberWisieStatus.NOW_KNOW_ANSWER) {
                addState(new WisieStateNowKnowAnswer(manager)).addOnFlowableEndListener(aLong9 -> {
                    addState(new WisieStateAnsweringPhase5(manager)).startVoid();
                }).startFlowable();
            } else if (aa4 == MemberWisieStatus.DOESNT_KNOW_ANSWER) {
                phase6();
            }
        }).startFlowable();
    }

    private synchronized void phase6() {
        addState(new WisieStateDoesntKnowAnswer(manager)).addOnFlowableEndListener(aLong10 -> {
            addState(new WisieStateEndThinkingIfGiveRandomAnswer(manager)).addOnFlowableEndListener(aLong11 -> {
                MemberWisieStatus aa5 = addState(new WisieStateCheckIfGiveRandomAnswer(manager)).startWisieAnswerAction();
                if (aa5 == MemberWisieStatus.WILL_GIVE_RANDOM_ANSWER) {
                    addState(new WisieStateAnsweringPhase6(manager)).startVoid();
                } else if (aa5 == MemberWisieStatus.WONT_GIVE_RANDOM_ANSWER) {
                    addState(new WisieStateSurrender(manager)).startVoid();
                }
            }).startFlowable();
        }).startFlowable();
    }

}
