package com.ww.game.play.state.skill.pizza;

import com.ww.game.member.command.MemberWisieAddStatusCommand;
import com.ww.game.member.command.MemberWisieDecreaseAttributesCommand;
import com.ww.game.member.command.MemberWisieRemoveDisguiseCommand;
import com.ww.game.play.flow.skill.PlaySkillFlowOpponent;
import com.ww.game.play.state.skill.PlaySkillOpponentState;
import com.ww.model.constant.wisie.MemberWisieStatus;
import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.war.WarTeam;

import java.util.HashMap;
import java.util.Map;

import static com.ww.game.play.modelfiller.PlayWarModelFiller.fillModelActiveMemberAddOns;
import static com.ww.game.play.modelfiller.PlayWarModelFiller.fillModelTeams;

public class PlaySkillEatenPizzaState extends PlaySkillOpponentState {
    public PlaySkillEatenPizzaState(PlaySkillFlowOpponent flow) {
        super(flow);
    }

    @Override
    public void initCommands() {
        commands.add(new MemberWisieRemoveDisguiseCommand(opponentManager));
        commands.add(new MemberWisieDecreaseAttributesCommand(opponentManager));
        commands.add(new MemberWisieAddStatusCommand(opponentManager, MemberWisieStatus.EATEN_PIZZA));
    }

    @Override
    public Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam) {
        Map<String, Object> model = super.prepareModel(opponentWarTeam, team, opponentTeam);
        fillModelActiveMemberAddOns(model, opponentWarTeam, (WarTeam) team, (WarTeam) opponentTeam);
        fillModelTeams(model, opponentWarTeam, (WarTeam) team, (WarTeam) opponentTeam);
        return model;
    }

    @Override
    protected double prepareInterval() {
        return 2;
    }

    @Override
    public void after() {
        Map<String,Object> params = new HashMap<>();
        params.put("profileId", opponentWarTeam.getProfileId());
        flow.run("DONE_PIZZA", params);
    }
}
