package com.ww.game.play.state.skill.ninja;

import com.ww.game.member.MemberWisieManager;
import com.ww.game.member.command.MemberWisieAddStatusCommand;
import com.ww.game.play.flow.skill.PlaySkillFlow;
import com.ww.game.play.state.skill.PlaySkillState;
import com.ww.model.constant.wisie.MemberWisieStatus;

public class PlaySkillPreparingNinjaState extends PlaySkillState {

    public PlaySkillPreparingNinjaState(PlaySkillFlow flow, MemberWisieManager manager) {
        super(flow, manager);
    }

    @Override
    public void initCommands() {
        commands.add(new MemberWisieAddStatusCommand(manager, MemberWisieStatus.PREPARING_NINJA));
    }

    @Override
    protected double minInterval() {
        return 3 - getWisie().getSpeedF1() - getWisie().getCunningF1();
    }

    @Override
    protected double maxInterval() {
        return 4 - getWisie().getSpeedF1() - 2 * getWisie().getCunningF1();
    }

    @Override
    public void after() {
        flow.run("KIDNAPPING");
    }
}
