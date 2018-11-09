package com.ww.game.play.state.skill.ghost;

import com.ww.game.member.MemberWisieManager;
import com.ww.game.member.command.MemberWisieAddStatusCommand;
import com.ww.game.play.flow.skill.PlaySkillFlow;
import com.ww.game.play.state.skill.PlaySkillState;
import com.ww.model.constant.wisie.MemberWisieStatus;

public class PlaySkillPreparingGhostState extends PlaySkillState {

    public PlaySkillPreparingGhostState(PlaySkillFlow flow, MemberWisieManager manager) {
        super(flow, manager);
    }

    @Override
    public void initCommands() {
        commands.add(new MemberWisieAddStatusCommand(manager, MemberWisieStatus.PREPARING_GHOST));
    }

    @Override
    protected double minInterval() {
        return 2 - getWisie().getSpeedF1() - getWisie().getCunningF1();
    }

    @Override
    protected double maxInterval() {
        return 3 - getWisie().getSpeedF1() - 2 * getWisie().getCunningF1();
    }

    @Override
    public void after() {
        flow.run("SCARING");
    }
}
