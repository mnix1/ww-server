package com.ww.game.play.state.skill.pizza;

import com.ww.game.member.command.MemberWisieAddDisguiseCommand;
import com.ww.game.member.command.MemberWisieAddStatusCommand;
import com.ww.game.member.command.MemberWisieDecreaseAttributesCommand;
import com.ww.game.play.flow.skill.PlaySkillFlowOpponent;
import com.ww.game.play.state.skill.PlaySkillOpponentState;
import com.ww.model.constant.wisie.DisguiseType;
import com.ww.model.constant.wisie.MemberWisieStatus;
import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.war.WarTeam;

import java.util.Map;

import static com.ww.game.play.modelfiller.PlayWarModelFiller.fillModelActiveMemberAddOns;
import static com.ww.game.play.modelfiller.PlayWarModelFiller.fillModelTeams;

public class PlaySkillServingPizzaState extends PlaySkillOpponentState {
    public PlaySkillServingPizzaState(PlaySkillFlowOpponent flow) {
        super(flow);
    }

    @Override
    public void initCommands() {
        commands.add(new MemberWisieAddStatusCommand(manager, MemberWisieStatus.SERVING_PIZZA));
        commands.add(new MemberWisieAddStatusCommand(opponentManager, MemberWisieStatus.PREPARING_FOR_EAT_PIZZA));
        commands.add(new MemberWisieAddDisguiseCommand(opponentManager, DisguiseType.PIZZA_MAN));
        commands.add(new MemberWisieDecreaseAttributesCommand(opponentManager));
    }

    @Override
    public Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam) {
        Map<String, Object> model = super.prepareModel(team, opponentTeam);
        fillModelActiveMemberAddOns(model, opponentWarTeam, (WarTeam) team, (WarTeam) opponentTeam);
        fillModelTeams(model, opponentWarTeam, (WarTeam) team, (WarTeam) opponentTeam);
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
        flow.run("CLEANING_PIZZA");
        flow.run("EATING_PIZZA");
    }
}
