package com.ww.manager.wisieanswer.skill;

import com.ww.manager.AbstractState;
import com.ww.manager.wisieanswer.WisieAnswerFlow;
import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.skill.state.waterpistol.WisieStateCleaned;
import com.ww.manager.wisieanswer.skill.state.waterpistol.WisieStateCleaning;
import com.ww.manager.wisieanswer.skill.state.waterpistol.WisieStateWaterPistolUsedOnIt;
import lombok.Getter;

@Getter
public class WisieAnswerWaterPistolSkillFlow {

    private WisieAnswerFlow flow;
    private WisieAnswerManager manager;

    private boolean waterPistolUsedOnIt = false;

    public WisieAnswerWaterPistolSkillFlow(WisieAnswerFlow flow) {
        this.flow = flow;
        this.manager = flow.getManager();
    }

    private synchronized void phaseWaterPistol() {
        AbstractState prevState = flow.lastFlowableState();
        flow.addState(new WisieStateWaterPistolUsedOnIt(manager)).addOnFlowableEndListener(aLong1 -> {
            flow.addState(new WisieStateCleaning(manager)).addOnFlowableEndListener(aLong2 -> {
                flow.addState(new WisieStateCleaned(manager)).startVoid();
                prevState.startFlowableEndListeners();
            }).startFlowable();
        }).startFlowable();
    }

    public void waterPistol() {
        if (waterPistolUsedOnIt) {
            return;
        }
        flow.dispose();
        waterPistolUsedOnIt = true;
        phaseWaterPistol();
    }

}
