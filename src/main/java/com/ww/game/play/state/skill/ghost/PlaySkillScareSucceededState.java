package com.ww.game.play.state.skill.ghost;

import com.ww.game.member.MemberWisieManager;
import com.ww.game.member.command.MemberWisieAddDisguiseCommand;
import com.ww.game.member.command.MemberWisieAddStatusCommand;
import com.ww.game.play.command.war.PlayWarDisableActiveTeamMemberCommand;
import com.ww.game.play.flow.skill.PlaySkillFlow;
import com.ww.game.play.state.skill.PlaySkillOpponentState;
import com.ww.model.constant.wisie.DisguiseType;
import com.ww.model.constant.wisie.MemberWisieStatus;

import java.util.HashMap;
import java.util.Map;

public class PlaySkillScareSucceededState extends PlaySkillOpponentState {
    public PlaySkillScareSucceededState(PlaySkillFlow flow, MemberWisieManager manager, MemberWisieManager opponentManager) {
        super(flow, manager, opponentManager);
    }

    @Override
    public void initCommands() {
        commands.add(new MemberWisieAddStatusCommand(manager, MemberWisieStatus.SCARE_SUCCEEDED));
        commands.add(new MemberWisieAddStatusCommand(opponentManager, MemberWisieStatus.RUN_AWAY));
        commands.add(new MemberWisieAddDisguiseCommand(opponentManager, DisguiseType.CHAIR_RED));
        commands.add(new PlayWarDisableActiveTeamMemberCommand(opponentManager.getContainer().getTeam()));
    }

    @Override
    protected double minInterval() {
        return 1 - getWisie().getReflexF1();
    }

    @Override
    protected double maxInterval() {
        return 2 - 2 * getWisie().getReflexF1();
    }

    @Override
    public void after() {
        Map<String,Object> params = new HashMap<>();
        params.put("scareSuccess", true);
        flow.run("REMOVING_GHOST", params);
    }
}
