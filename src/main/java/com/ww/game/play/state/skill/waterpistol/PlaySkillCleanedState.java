package com.ww.game.play.state.skill.waterpistol;

import com.ww.game.member.MemberWisieManager;
import com.ww.game.member.command.MemberWisieAddDisguiseCommand;
import com.ww.game.member.command.MemberWisieAddStatusCommand;
import com.ww.game.member.command.MemberWisieRemoveDisguiseCommand;
import com.ww.game.play.command.skill.PlaySkillBlockAllCommand;
import com.ww.game.play.command.skill.PlaySkillUnblockAllCommand;
import com.ww.game.play.flow.skill.PlaySkillFlow;
import com.ww.game.play.state.skill.PlaySkillState;
import com.ww.model.constant.wisie.DisguiseType;
import com.ww.model.constant.wisie.MemberWisieStatus;
import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.war.WarTeam;

import java.util.HashMap;
import java.util.Map;

import static com.ww.game.play.modelfiller.PlayWarModelFiller.fillModelActiveMemberAddOns;
import static com.ww.game.play.modelfiller.PlayWarModelFiller.fillModelSkills;

public class PlaySkillCleanedState extends PlaySkillState {

    public PlaySkillCleanedState(PlaySkillFlow flow, MemberWisieManager manager) {
        super(flow, manager);
    }

    @Override
    public void initCommands() {
        commands.add(new MemberWisieRemoveDisguiseCommand(manager));
        commands.add(new PlaySkillUnblockAllCommand(warTeam));
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
        flow.notifyOuter(manager.getFlow());
    }
}
