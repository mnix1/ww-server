package com.ww.game.member.flow;

import com.ww.game.GameFlow;
import com.ww.game.GameState;
import com.ww.game.member.MemberWisieManager;
import com.ww.game.member.container.MemberWisieContainer;
import com.ww.game.member.state.wisie.*;

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

    protected synchronized void addState(GameState state) {
        super.addState(state);
        manager.getPlayManager().getFlow().childStateChanged();
    }

    protected synchronized void waitingForQuestionPhase() {
        MemberWisieIntervalState state = new MemberWisieWaitingForQuestionState(getContainer());
        addState(state);
        after(state.getInterval(), aLong -> recognizingQuestionPhase());
    }

    protected synchronized void recognizingQuestionPhase() {
        MemberWisieIntervalState state = new MemberWisieRecognizingQuestionState(getContainer());
        addState(state);
        after(state.getInterval(), aLong -> thinkingPhase());
    }

    protected synchronized void thinkingPhase() {
        MemberWisieIntervalState state = new MemberWisieThinkingState(getContainer());
        addState(state);
        after(state.getInterval(), aLong -> thinkKnowAnswerPhase());
    }

    protected synchronized void thinkKnowAnswerPhase() {
        MemberWisieIntervalState state = new MemberWisieThinkKnowAnswerState(getContainer());
        addState(state);
        after(state.getInterval(), aLong -> lookingForAnswerPhase());
    }

    protected synchronized void lookingForAnswerPhase() {
        MemberWisieIntervalState state = new MemberWisieLookingForAnswerState(getContainer());
        addState(state);
        after(state.getInterval(), aLong -> foundAnswerLookingForPhase());
    }

    protected synchronized void foundAnswerLookingForPhase() {
        MemberWisieIntervalState state = new MemberWisieFoundAnswerLookingForState(getContainer());
        addState(state);
//        after(state.getInterval(), aLong ->);
    }


}
