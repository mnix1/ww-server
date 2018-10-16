package com.ww.model.container.rival.challenge;

import com.ww.manager.rival.challenge.state.ChallengeStateChoosingWhoAnswer;
import com.ww.manager.rival.war.WarFlow;
import com.ww.manager.rival.war.WarManager;
import lombok.Getter;

@Getter
public class ChallengeFlow extends WarFlow {

    public ChallengeFlow(WarManager manager) {
        super(manager);
    }

    public synchronized void phase3() {
        addState(new ChallengeStateChoosingWhoAnswer(manager)).addOnFlowableEndListener(aLong2 -> {
            phase1();
        }).startFlowable();
    }
}
