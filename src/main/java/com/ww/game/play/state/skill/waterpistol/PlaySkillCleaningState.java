package com.ww.game.play.state.skill.waterpistol;

import com.ww.game.member.MemberWisieManager;
import com.ww.game.member.command.MemberWisieAddStatusCommand;
import com.ww.game.play.flow.skill.PlaySkillFlow;
import com.ww.game.play.state.skill.PlaySkillState;
import com.ww.model.constant.wisie.MemberWisieStatus;

public class PlaySkillCleaningState extends PlaySkillState {

    public PlaySkillCleaningState(PlaySkillFlow flow, MemberWisieManager manager) {
        super(flow, manager);
    }

    @Override
    public void initCommands() {
        commands.add(new MemberWisieAddStatusCommand(manager, MemberWisieStatus.CLEANING));
    }

    @Override
    protected double minInterval() {
        return 8 - 4 * getWisie().getSpeedF1() - 4 * getWisie().getCunningF1();
    }

    @Override
    protected double maxInterval() {
        return 16 - 8 * getWisie().getSpeedF1() - 8 * getWisie().getCunningF1();
    }

    @Override
    public void after() {
        flow.run("CLEANED");
    }
}
