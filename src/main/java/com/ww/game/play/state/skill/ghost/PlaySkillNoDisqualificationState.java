package com.ww.game.play.state.skill.ghost;

import com.ww.game.member.MemberWisieManager;
import com.ww.game.member.command.MemberWisieAddStatusCommand;
import com.ww.game.member.command.MemberWisieRemoveDisguiseCommand;
import com.ww.game.play.command.skill.PlaySkillUnblockAllCommand;
import com.ww.game.play.flow.skill.PlaySkillFlow;
import com.ww.game.play.state.skill.PlaySkillState;
import com.ww.model.constant.wisie.MemberWisieStatus;

public class PlaySkillNoDisqualificationState extends PlaySkillState {
    public PlaySkillNoDisqualificationState(PlaySkillFlow flow, MemberWisieManager manager) {
        super(flow, manager);
    }

    @Override
    public void initCommands() {
        commands.add(new MemberWisieAddStatusCommand(manager, MemberWisieStatus.NO_DISQUALIFICATION));
        commands.add(new MemberWisieRemoveDisguiseCommand(manager));
        commands.add(new PlaySkillUnblockAllCommand(manager.getContainer().getTeam()));
    }

    @Override
    protected double minInterval() {
        return 2 - getWisie().getReflexF1() - getWisie().getCunningF1();
    }

    @Override
    protected double maxInterval() {
        return 4 - 2 * getWisie().getReflexF1() - 2 * getWisie().getCunningF1();
    }

    @Override
    public void after() {
        flow.notifyOuter(manager.getFlow());
    }
}
