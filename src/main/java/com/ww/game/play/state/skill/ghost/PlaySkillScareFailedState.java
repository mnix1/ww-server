package com.ww.game.play.state.skill.ghost;

import com.ww.game.member.MemberWisieManager;
import com.ww.game.member.command.MemberWisieAddDisguiseCommand;
import com.ww.game.member.command.MemberWisieAddStatusCommand;
import com.ww.game.play.command.skill.PlaySkillUnblockAllCommand;
import com.ww.game.play.command.war.PlayWarDisableActiveTeamMemberCommand;
import com.ww.game.play.flow.skill.PlaySkillFlow;
import com.ww.game.play.state.skill.PlaySkillOpponentState;
import com.ww.model.constant.wisie.DisguiseType;
import com.ww.model.constant.wisie.MemberWisieStatus;
import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.war.WarTeam;

import java.util.HashMap;
import java.util.Map;

import static com.ww.game.play.modelfiller.PlayWarModelFiller.fillModelSkills;

public class PlaySkillScareFailedState extends PlaySkillOpponentState {
    public PlaySkillScareFailedState(PlaySkillFlow flow, MemberWisieManager manager, MemberWisieManager opponentManager) {
        super(flow, manager, opponentManager);
    }

    @Override
    public void initCommands() {
        commands.add(new MemberWisieAddStatusCommand(manager, MemberWisieStatus.SCARE_FAILED));
        commands.add(new MemberWisieAddStatusCommand(opponentManager, MemberWisieStatus.WAS_NOT_SCARED));
        commands.add(new PlaySkillUnblockAllCommand(opponentManager.getContainer().getTeam()));
    }

    @Override
    public Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam) {
        Map<String, Object> model = super.prepareModel(team, opponentTeam);
        fillModelSkills(model, (WarTeam) team, (WarTeam) opponentTeam);
        return model;
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
        flow.run("WAS_NOT_SCARED");
        Map<String, Object> params = new HashMap<>();
        params.put("scareSuccess", false);
        flow.run("REMOVING_GHOST", params);
    }
}
