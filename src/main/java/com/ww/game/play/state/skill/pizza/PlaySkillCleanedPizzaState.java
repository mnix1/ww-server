package com.ww.game.play.state.skill.pizza;

import com.ww.game.member.MemberWisieManager;
import com.ww.game.member.command.MemberWisieAddStatusCommand;
import com.ww.game.member.command.MemberWisieRemoveDisguiseCommand;
import com.ww.game.member.command.MemberWisieNotifyOuterFlowCommand;
import com.ww.game.play.command.skill.PlaySkillUnblockAllCommand;
import com.ww.game.play.flow.skill.PlaySkillFlow;
import com.ww.game.play.state.skill.PlaySkillState;
import com.ww.model.constant.wisie.MemberWisieStatus;
import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.war.WarTeam;

import java.util.Map;

import static com.ww.game.play.modelfiller.PlayWarModelFiller.fillModelActiveMemberAddOns;
import static com.ww.game.play.modelfiller.PlayWarModelFiller.fillModelSkills;

public class PlaySkillCleanedPizzaState extends PlaySkillState {
    public PlaySkillCleanedPizzaState(PlaySkillFlow flow, MemberWisieManager manager) {
        super(flow, manager);
    }

    @Override
    public void initCommands() {
        commands.add(new MemberWisieRemoveDisguiseCommand(manager));
        commands.add(new MemberWisieAddStatusCommand(manager, MemberWisieStatus.CLEANED_AFTER_PIZZA));
        commands.add(new PlaySkillUnblockAllCommand(warTeam));
        commands.add(new MemberWisieNotifyOuterFlowCommand(flow, manager));
    }

    @Override
    public Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam) {
        Map<String, Object> model = super.prepareModel(warTeam, team, opponentTeam);
        fillModelActiveMemberAddOns(model, warTeam, (WarTeam) team, (WarTeam) opponentTeam);
        fillModelSkills(model, warTeam, (WarTeam) team, (WarTeam) opponentTeam);
        return model;
    }

    @Override
    protected double prepareInterval() {
        return 1;
    }

    @Override
    public void after() {
        if (!manager.getFlow().hasNext()) {
            manager.getFlow().get().currentState().after();
        }
    }
}
