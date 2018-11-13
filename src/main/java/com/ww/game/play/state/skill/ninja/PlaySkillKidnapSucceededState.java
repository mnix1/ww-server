package com.ww.game.play.state.skill.ninja;

import com.ww.game.member.MemberWisieManager;
import com.ww.game.member.command.MemberWisieAddDisguiseCommand;
import com.ww.game.member.command.MemberWisieAddStatusCommand;
import com.ww.game.play.command.war.PlayWarDisableActiveTeamMemberCommand;
import com.ww.game.play.flow.PlayWarFlow;
import com.ww.game.play.flow.skill.PlaySkillFlow;
import com.ww.game.play.state.skill.PlaySkillOpponentState;
import com.ww.model.constant.wisie.DisguiseType;
import com.ww.model.constant.wisie.MemberWisieStatus;
import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.war.WarTeam;

import java.util.HashMap;
import java.util.Map;

import static com.ww.game.play.modelfiller.PlayWarModelFiller.fillModelActiveMemberAddOns;

public class PlaySkillKidnapSucceededState extends PlaySkillOpponentState {
    public PlaySkillKidnapSucceededState(PlaySkillFlow flow, MemberWisieManager manager, MemberWisieManager opponentManager) {
        super(flow, manager, opponentManager);
    }

    @Override
    public void initCommands() {
        commands.add(new MemberWisieAddStatusCommand(manager, MemberWisieStatus.KIDNAPPING_SUCCEEDED));
        commands.add(new MemberWisieAddStatusCommand(opponentManager, MemberWisieStatus.WAS_KIDNAPPED));
        commands.add(new MemberWisieAddDisguiseCommand(manager, DisguiseType.CHAIR_SIMPLE));
        commands.add(new MemberWisieAddDisguiseCommand(opponentManager, DisguiseType.CHAIR_SIMPLE));
        commands.add(new PlayWarDisableActiveTeamMemberCommand(manager.getContainer().getTeam()));
        commands.add(new PlayWarDisableActiveTeamMemberCommand(opponentManager.getContainer().getTeam()));
    }

    @Override
    public Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam) {
        Map<String, Object> model = super.prepareModel(team, opponentTeam);
        fillModelActiveMemberAddOns(model, (WarTeam) team, (WarTeam) opponentTeam);
        return model;
    }

    @Override
    protected double prepareInterval() {
        return 3;
    }

    @Override
    public void after() {
        ((PlayWarFlow) manager.getPlayManager().getFlow()).wisiesWontAnswer();
    }
}
