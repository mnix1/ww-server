package com.ww.game.play.state.skill.pizza;

import com.ww.game.member.MemberWisieManager;
import com.ww.game.play.flow.skill.PlaySkillFlow;
import com.ww.game.play.flow.skill.PlaySkillFlowOpponent;
import com.ww.game.play.state.skill.PlaySkillOpponentState;
import com.ww.game.play.state.skill.PlaySkillState;

public class PlaySkillEatingPizzaState extends PlaySkillOpponentState {

    public PlaySkillEatingPizzaState(PlaySkillFlowOpponent flow) {
        super(flow);
    }

    @Override
    protected double minInterval() {
        return 9 - 8 * getOpponentWisie().getSpeedF1();
    }

    @Override
    protected double maxInterval() {
        return 17 - 16 * getOpponentWisie().getSpeedF1();
    }

    @Override
    public void updateNotify() {
    }

    @Override
    public void after() {
        flow.run("EATEN_PIZZA");
    }
}
