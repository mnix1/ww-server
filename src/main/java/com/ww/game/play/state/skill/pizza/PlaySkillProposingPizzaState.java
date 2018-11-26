package com.ww.game.play.state.skill.pizza;

import com.ww.game.member.command.MemberWisieAddDisguiseCommand;
import com.ww.game.member.command.MemberWisieAddStatusCommand;
import com.ww.game.member.command.MemberWisieRunInnerFlowCommand;
import com.ww.game.play.command.skill.PlaySkillBlockAllCommand;
import com.ww.game.play.flow.skill.PlaySkillFlowOpponent;
import com.ww.game.play.state.skill.PlaySkillOpponentState;
import com.ww.model.constant.wisie.DisguiseType;
import com.ww.model.constant.wisie.MemberWisieStatus;
import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.war.WarTeam;

import java.util.Map;

import static com.ww.game.play.modelfiller.PlayWarModelFiller.fillModelActiveMemberAddOns;
import static com.ww.game.play.modelfiller.PlayWarModelFiller.fillModelSkills;

public class PlaySkillProposingPizzaState extends PlaySkillOpponentState {
    public PlaySkillProposingPizzaState(PlaySkillFlowOpponent flow) {
        super(flow);
    }

    @Override
    public void initCommands() {
        commands.add(new MemberWisieAddStatusCommand(manager, MemberWisieStatus.PROPOSING_PIZZA));
        commands.add(new MemberWisieRunInnerFlowCommand(flow, opponentWarTeam.getProfileId()));
        commands.add(new MemberWisieAddStatusCommand(opponentManager, MemberWisieStatus.THINKING_IF_GET_PIZZA));
        commands.add(new PlaySkillBlockAllCommand(warTeam));
        commands.add(new PlaySkillBlockAllCommand(opponentWarTeam));
        commands.add(new MemberWisieAddDisguiseCommand(manager, DisguiseType.PIZZA_COOK));
    }

    @Override
    public Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam) {
        Map<String, Object> model = super.prepareModel(team, opponentTeam);
        fillModelSkills(model, (WarTeam) team, (WarTeam) opponentTeam);
        fillModelActiveMemberAddOns(model, warTeam, (WarTeam) team, (WarTeam) opponentTeam);
        return model;
    }

    @Override
    protected double minInterval() {
        return 2;
    }

    @Override
    protected double maxInterval() {
        return 3;
    }

    @Override
    public void after() {
        flow.run("SERVING_PIZZA");
    }
}
