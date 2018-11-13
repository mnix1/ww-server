package com.ww.game.play.state.skill.ninja;

import com.ww.game.member.MemberWisieManager;
import com.ww.game.member.command.MemberWisieAddStatusCommand;
import com.ww.game.member.command.MemberWisieRemoveDisguiseCommand;
import com.ww.game.play.command.skill.PlaySkillUnblockAllCommand;
import com.ww.game.play.flow.skill.PlaySkillFlow;
import com.ww.game.play.state.skill.PlaySkillState;
import com.ww.model.constant.wisie.MemberWisieStatus;
import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.war.WarTeam;

import java.util.Map;

import static com.ww.game.play.modelfiller.PlayWarModelFiller.fillModelActiveMemberAddOns;
import static com.ww.game.play.modelfiller.PlayWarModelFiller.fillModelSkills;

public class PlaySkillRemovingNinjaState extends PlaySkillState {
    public PlaySkillRemovingNinjaState(PlaySkillFlow flow, MemberWisieManager manager) {
        super(flow, manager);
    }

    @Override
    public void initCommands() {
        commands.add(new MemberWisieAddStatusCommand(manager, MemberWisieStatus.REMOVING_NINJA));
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
        flow.run("REMOVED_NINJA");
    }
}
