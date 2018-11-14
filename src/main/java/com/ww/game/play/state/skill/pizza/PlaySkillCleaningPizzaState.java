package com.ww.game.play.state.skill.pizza;

import com.ww.game.member.MemberWisieManager;
import com.ww.game.member.command.MemberWisieAddStatusCommand;
import com.ww.game.member.command.MemberWisieRemoveDisguiseCommand;
import com.ww.game.play.flow.skill.PlaySkillFlow;
import com.ww.game.play.state.skill.PlaySkillOpponentState;
import com.ww.model.constant.wisie.MemberWisieStatus;
import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.war.WarTeam;

import java.util.Map;

import static com.ww.game.play.modelfiller.PlayWarModelFiller.fillModelActiveMemberAddOns;

public class PlaySkillCleaningPizzaState extends PlaySkillOpponentState {
    public PlaySkillCleaningPizzaState(PlaySkillFlow flow, MemberWisieManager manager, MemberWisieManager opponentManager) {
        super(flow, manager, opponentManager);
    }

    @Override
    public void initCommands() {
        commands.add(new MemberWisieAddStatusCommand(manager, MemberWisieStatus.CLEANING_AFTER_PIZZA));
        commands.add(new MemberWisieAddStatusCommand(opponentManager, MemberWisieStatus.EATING_PIZZA));
    }

    @Override
    public Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam) {
        Map<String, Object> model = super.prepareModel(team, opponentTeam);
        fillModelActiveMemberAddOns(model, (WarTeam) team, (WarTeam) opponentTeam);
        return model;
    }

    @Override
    protected double minInterval() {
        return 2 - getWisie().getSpeedF1();
    }

    @Override
    protected double maxInterval() {
        return 4 - 2 * getWisie().getSpeedF1();
    }

    @Override
    public void after() {
        flow.run("CLEANED_PIZZA");
    }
}
