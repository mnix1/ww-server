package com.ww.game.play.flow.skill;

import com.ww.game.member.MemberWisieManager;
import com.ww.game.play.state.skill.pizza.*;

public class PlaySkillPizzaFlow extends PlaySkillFlow {
    protected MemberWisieManager opponentManager;

    public PlaySkillPizzaFlow(MemberWisieManager manager, MemberWisieManager opponentManager) {
        super(manager);
        this.opponentManager = opponentManager;
        initStateMap();
    }

    @Override
    protected void initStateMap() {
        stateMap.put("ORDERING_PIZZA",() ->  new PlaySkillOrderingPizzaState(this, manager));
        stateMap.put("PROPOSING_PIZZA",() ->  new PlaySkillProposingPizzaState(this, manager, opponentManager));
        stateMap.put("SERVING_PIZZA",() ->  new PlaySkillServingPizzaState(this, manager, opponentManager));
        stateMap.put("CLEANING_PIZZA",() ->  new PlaySkillCleaningPizzaState(this, manager, opponentManager));
        stateMap.put("CLEANED_PIZZA",() ->  new PlaySkillCleanedPizzaState(this, manager));
        stateMap.put("EATING_PIZZA", () -> new PlaySkillEatingPizzaState(this, opponentManager));
        stateMap.put("EATEN_PIZZA", () -> new PlaySkillEatenPizzaState(this, opponentManager));
    }

    @Override
    public void start() {
        super.start();
        run("ORDERING_PIZZA");
    }
}
