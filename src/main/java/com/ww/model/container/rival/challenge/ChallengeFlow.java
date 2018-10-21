package com.ww.model.container.rival.challenge;

import com.ww.manager.rival.challenge.state.ChallengeStateAnswered;
import com.ww.manager.rival.challenge.state.ChallengeStateChoosingWhoAnswer;
import com.ww.manager.rival.war.WarFlow;
import com.ww.manager.rival.war.WarManager;
import com.ww.manager.rival.war.state.WarStateAnswered;
import lombok.Getter;

import java.util.Map;

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

    public synchronized void answer(Long profileId, Map<String, Object> content) {
        logger.trace(describe() + manager.describe() + " answer, profileId={}", profileId);
        dispose();
        addState(new ChallengeStateAnswered(manager, profileId, content)).addOnFlowableEndListener(aLong -> {
            phase2();
        }).startFlowable();
    }

}
