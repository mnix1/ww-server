package com.ww.game.member.flow;

import com.ww.game.GameFlow;
import com.ww.game.member.MemberWisieManager;
import com.ww.game.member.container.MemberWisieContainer;
import com.ww.game.member.state.wisie.MemberWisieState;
import com.ww.game.member.state.wisie.MemberWisieRecognizingQuestionState;
import com.ww.game.member.state.wisie.MemberWisieWaitingForQuestionState;

public class MemberWisieFlow extends GameFlow {
    protected MemberWisieManager manager;

    public MemberWisieFlow(MemberWisieManager manager) {
        this.manager = manager;
    }

    @Override
    protected MemberWisieContainer getContainer() {
        return (MemberWisieContainer) manager.getContainer();
    }

    public void start() {
        waitingForQuestionPhase();
    }

    protected void waitingForQuestionPhase() {
        MemberWisieState state = new MemberWisieWaitingForQuestionState(getContainer());
        addState(state);
        after(state.getInterval(), aLong -> recognizingQuestionPhase());
    }

    protected void recognizingQuestionPhase() {
        MemberWisieState state = new MemberWisieRecognizingQuestionState(getContainer());
        addState(state);
        after(state.getInterval(), aLong -> thinkingPhase());
    }

    protected void thinkingPhase() {
//        MemberWisieState state = new MemberWisieRecognizingQuestionState(getContainer());
//        addState(state);
//        after(state.interval(), aLong ->);
    }


}
