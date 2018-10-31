package com.ww.game.member.flow;

import com.ww.game.GameContainer;
import com.ww.game.GameFlow;
import com.ww.game.GameState;
import com.ww.game.member.MemberManager;

public class MemberFlow extends GameFlow {
    protected MemberManager manager;

    @Override
    protected GameContainer getContainer() {
        return manager.getContainer();
    }

    public void recognizingQuestionPhase() {
        addState(createRecognizingQuestionState());
        afterRecognizingQuestionPhase();
    }

    protected GameState createRecognizingQuestionState() {

    }

    protected void afterRecognizingQuestionPhase() {

    }

}
