package com.ww.game.play.state.skill.ghost;

import com.ww.game.member.MemberWisieManager;
import com.ww.game.member.command.MemberWisieAddStatusCommand;
import com.ww.game.play.command.skill.PlaySkillUnblockAllCommand;
import com.ww.game.play.flow.skill.PlaySkillFlow;
import com.ww.game.play.state.skill.PlaySkillState;
import com.ww.model.constant.wisie.MemberWisieStatus;
import com.ww.model.container.rival.RivalTeam;

import java.util.Map;

public class PlaySkillWasNotScaredState extends PlaySkillState {

    public PlaySkillWasNotScaredState(PlaySkillFlow flow, MemberWisieManager manager) {
        super(flow, manager);
    }

    @Override
    protected double minInterval() {
        return 2 - getWisie().getReflexF1() - getWisie().getConcentrationF1();
    }

    @Override
    protected double maxInterval() {
        return 3 - getWisie().getReflexF1() - 2 * getWisie().getConcentrationF1();
    }

    @Override
    public void updateNotify() {
    }

    @Override
    public void after() {
        flow.notifyOuter(manager.getFlow());
    }
}
