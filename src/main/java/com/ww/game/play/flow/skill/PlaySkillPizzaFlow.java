package com.ww.game.play.flow.skill;

import com.ww.game.play.container.skill.PlayWarAnsweringFlowContainer;
import com.ww.game.play.state.skill.pizza.*;

public class PlaySkillPizzaFlow extends PlaySkillFlowOpponent {
    public PlaySkillPizzaFlow(PlayWarAnsweringFlowContainer flowContainer, Long creatorProfileId, Long opponentProfileId) {
        super(flowContainer, creatorProfileId, opponentProfileId);
    }

    @Override
    protected void initStateMap() {
        stateMap.put("ORDERING_PIZZA",() ->  new PlaySkillOrderingPizzaState(this));
        stateMap.put("PROPOSING_PIZZA",() ->  new PlaySkillProposingPizzaState(this));
        stateMap.put("SERVING_PIZZA",() ->  new PlaySkillServingPizzaState(this));
        stateMap.put("CLEANING_PIZZA",() ->  new PlaySkillCleaningPizzaState(this));
        stateMap.put("CLEANED_PIZZA",() ->  new PlaySkillCleanedPizzaState(this));
        stateMap.put("EATING_PIZZA", () -> new PlaySkillEatingPizzaState(this));
        stateMap.put("EATEN_PIZZA", () -> new PlaySkillEatenPizzaState(this));
    }

    @Override
    public void start() {
        super.start();
        run("ORDERING_PIZZA");
    }
}
