package com.ww.game.play.state.skill.ninja;

import com.ww.game.member.command.MemberWisieRemoveDisguiseCommand;
import com.ww.game.member.command.MemberWisieRemoveMostOuterFlowCommand;
import com.ww.game.play.command.skill.PlaySkillUnblockAllCommand;
import com.ww.game.play.flow.skill.PlaySkillFlow;
import com.ww.game.play.state.skill.PlaySkillState;
import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.war.WarTeam;

import java.util.HashMap;
import java.util.Map;

import static com.ww.game.play.modelfiller.PlayWarModelFiller.fillModelActiveMemberAddOns;
import static com.ww.game.play.modelfiller.PlayWarModelFiller.fillModelSkills;

public class PlaySkillRemovedNinjaState extends PlaySkillState {
    public PlaySkillRemovedNinjaState(PlaySkillFlow flow) {
        super(flow);
    }

    @Override
    public void initCommands() {
        commands.add(new MemberWisieRemoveDisguiseCommand(manager));
        commands.add(new PlaySkillUnblockAllCommand(warTeam));
        commands.add(new MemberWisieRemoveMostOuterFlowCommand(flow, warTeam.getProfileId()));
    }

    @Override
    public Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam) {
        Map<String, Object> model = new HashMap<>();
        fillModelSkills(model, warTeam, (WarTeam) team, (WarTeam) opponentTeam);
        fillModelActiveMemberAddOns(model, warTeam, (WarTeam) team, (WarTeam) opponentTeam);
        return model;
    }

    @Override
    public void after() {
        flow.getFlowContainer().runMostOuter(warTeam.getProfileId());
    }
}
