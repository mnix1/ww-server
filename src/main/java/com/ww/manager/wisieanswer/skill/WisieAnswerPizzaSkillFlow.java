package com.ww.manager.wisieanswer.skill;

import com.ww.manager.AbstractState;
import com.ww.manager.wisieanswer.WisieAnswerFlow;
import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.skill.state.pizza.*;
import lombok.Getter;

@Getter
public class WisieAnswerPizzaSkillFlow {
    private WisieAnswerFlow flow;
    private WisieAnswerManager manager;

    private boolean pizzaUsed = false;

    public WisieAnswerPizzaSkillFlow(WisieAnswerFlow flow) {
        this.flow = flow;
        this.manager = flow.getManager();
    }

    private synchronized void phasePizza(WisieAnswerManager opponent) {
        AbstractState prevState = flow.lastFlowableState();
        flow.addState(new WisieStateOrderingPizza(manager)).addOnFlowableEndListener(aLong1 -> {
            WisieStateProposingPizza proposingPizzaState = new WisieStateProposingPizza(manager, opponent);
            AbstractState opponentPrevState = opponent.getFlow().lastFlowableState();
            opponentPrevState.dispose();
            flow.addState(proposingPizzaState).addOnFlowableEndListener(aLong2 -> {
                flow.addState(new WisieStateServesPizza(manager, opponent)).addOnFlowableEndListener(aLong3 -> {
                    flow.addState(new WisieStateWaitingForOpponentEatPizza(manager, opponent)).addOnFlowableEndListener(aLong4 -> {
                        flow.addState(new WisieStateCleaningAfterPizza(manager, opponent)).addOnFlowableEndListener(aLong5 -> {
                            flow.addState(new WisieStateCleanedAfterPizza(manager)).startVoid();
                            prevState.startFlowableEndListeners();
                        }).startFlowable();
                        opponentPrevState.startFlowableEndListeners();
                    }).startFlowable();
                }).startFlowable();
            }).startFlowable();
        }).startFlowable();
    }

    public void pizza(WisieAnswerManager opponent) {
        if (pizzaUsed) {
            return;
        }
        flow.dispose();
        pizzaUsed = true;
        phasePizza(opponent);
    }

}
