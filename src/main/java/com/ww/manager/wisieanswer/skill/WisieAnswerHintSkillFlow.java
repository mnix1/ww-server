package com.ww.manager.wisieanswer.skill;

import com.ww.manager.wisieanswer.WisieAnswerFlow;
import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.state.phase2.WisieStateStartThinkingAboutQuestion;
import com.ww.manager.wisieanswer.skill.state.hint.*;
import com.ww.model.constant.wisie.WisieAnswerAction;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
public class WisieAnswerHintSkillFlow {
    protected static final Logger logger = LoggerFactory.getLogger(WisieAnswerHintSkillFlow.class);

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

}
