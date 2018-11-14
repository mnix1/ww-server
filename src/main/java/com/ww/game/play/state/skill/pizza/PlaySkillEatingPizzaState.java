package com.ww.game.play.state.skill.pizza;

import com.ww.game.member.MemberWisieManager;
import com.ww.game.member.command.MemberWisieAddStatusCommand;
import com.ww.game.play.flow.skill.PlaySkillFlow;
import com.ww.game.play.state.skill.PlaySkillState;
import com.ww.model.constant.wisie.MemberWisieStatus;
import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.war.WarTeam;

import java.util.Map;

import static com.ww.game.play.modelfiller.PlayWarModelFiller.fillModelSkills;

public class PlaySkillEatingPizzaState extends PlaySkillState {

    public PlaySkillEatingPizzaState(PlaySkillFlow flow, MemberWisieManager manager) {
        super(flow, manager);
    }

    @Override
    protected double minInterval() {
        return 3 - getWisie().getSpeedF1();
    }

    @Override
    protected double maxInterval() {
        return 5 - 2 * getWisie().getSpeedF1();
    }

    @Override
    public void updateNotify(){
    }

    @Override
    public void after() {
        flow.run("EATEN_PIZZA");
    }
}
