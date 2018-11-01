package com.ww.manager.wisieanswer.skill;

import com.ww.manager.wisieanswer.WisieAnswerFlow;
import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.state.phase2.WisieStateStartThinkingAboutQuestion;
import com.ww.manager.wisieanswer.skill.state.hint.*;
import com.ww.model.constant.wisie.MemberWisieStatus;
import lombok.Getter;

@Getter
public class WisieAnswerHintSkillFlow {
    private WisieAnswerFlow flow;
    private WisieAnswerManager manager;

    private boolean receivedHint = false;
    private Long hintAnswerId;
    private boolean hintCorrect;

    public WisieAnswerHintSkillFlow(WisieAnswerFlow flow) {
        this.flow = flow;
        this.manager = flow.getManager();
    }

    private synchronized void phaseHint() {
        flow.addState(new WisieStateHintReceived(manager)).addOnFlowableEndListener(aLong10 -> {
            flow.addState(new WisieStateEndThinkingIfUseHint(manager, hintCorrect)).addOnFlowableEndListener(aLong11 -> {
                MemberWisieStatus aa5 = flow.addState(new WisieStateCheckIfUseHint(manager, hintCorrect)).startWisieAnswerAction();
                flow.addState(new WisieStateDecidedIfUseHint(manager, aa5)).addOnFlowableEndListener(aLong12 -> {
                    if (aa5 == MemberWisieStatus.WILL_USE_HINT) {
                        flow.addState(new WisieStateAnsweringUseHint(manager, hintCorrect, hintAnswerId)).startVoid();
                    } else if (aa5 == MemberWisieStatus.WONT_USE_HINT) {
                        flow.addState(new WisieStateStartThinkingAboutQuestion(manager)).addOnFlowableEndListener(aLong3 -> {
                            flow.addState(new WisieStateAnsweringNoUseHint(manager, hintAnswerId)).startVoid();
                        }).startFlowable();
                    }
                }).startFlowable();
            }).startFlowable();
        }).startFlowable();
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

}
