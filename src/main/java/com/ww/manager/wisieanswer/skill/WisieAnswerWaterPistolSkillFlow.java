package com.ww.manager.wisieanswer.skill;

import com.ww.manager.AbstractState;
import com.ww.manager.wisieanswer.WisieAnswerFlow;
import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.skill.state.waterpistol.WisieStateCleaning;
import com.ww.manager.wisieanswer.skill.state.waterpistol.WisieStateWaterPistolUsedOnIt;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Getter
public class WisieAnswerWaterPistolSkillFlow {
    protected static final Logger logger = LoggerFactory.getLogger(WisieAnswerWaterPistolSkillFlow.class);

    private WisieAnswerFlow flow;
    private WisieAnswerManager manager;

    private boolean waterPistolUsedOnIt = false;

    public WisieAnswerWaterPistolSkillFlow(WisieAnswerFlow flow) {
        this.flow = flow;
        this.manager = flow.getManager();
    }

    private synchronized void phaseWaterPistol() {
        AbstractState prevState = flow.getState();
        flow.setState(new WisieStateWaterPistolUsedOnIt(manager).addOnFlowableEndListener(aLong1 -> {
            flow.setState(new WisieStateCleaning(manager).addOnFlowableEndListener(aLong2 -> {
                flow.setState(prevState.startFlowable());
            }).startFlowable());
        }).startFlowable());
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
