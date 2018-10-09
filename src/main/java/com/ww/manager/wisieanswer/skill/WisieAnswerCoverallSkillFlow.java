package com.ww.manager.wisieanswer.skill;

import com.ww.manager.AbstractState;
import com.ww.manager.wisieanswer.WisieAnswerFlow;
import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.skill.state.WisieSkillState;
import com.ww.manager.wisieanswer.skill.state.coverall.WisieStateCoverallReady;
import com.ww.manager.wisieanswer.skill.state.coverall.WisieStatePuttingOnCoverall;
import com.ww.manager.wisieanswer.skill.state.pizza.*;
import lombok.Getter;

@Getter
public class WisieAnswerCoverallSkillFlow {
    private WisieAnswerFlow flow;
    private WisieAnswerManager manager;

    private boolean coverallUsed = false;

    public WisieAnswerCoverallSkillFlow(WisieAnswerFlow flow) {
        this.flow = flow;
        this.manager = flow.getManager();
    }

    private synchronized void phaseCoverall(WisieAnswerManager opponent) {
        AbstractState prevState = flow.lastFlowableState();
        flow.addState(new WisieStatePuttingOnCoverall(manager)).addOnFlowableEndListener(aLong1 -> {
            flow.addState(new WisieStateCoverallReady(manager, opponent)).addOnFlowableEndListener(aLong2 -> {
//                AbstractState opponentPrevState = opponent.getFlow().lastFlowableState();
//                if (opponentPrevState instanceof WisieSkillState) {
//                    opponentPrevState.dispose();
//                    ((WisieSkillState) opponentPrevState).runPrevState();
//                }
                prevState.startFlowableEndListeners();
            }).startFlowable();
        }).startFlowable();
    }

    public void coverall(WisieAnswerManager opponent) {
        if (coverallUsed) {
            return;
        }
        flow.dispose();
        coverallUsed = true;
        phaseCoverall(opponent);
    }

}
