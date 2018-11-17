package com.ww.game.play.state.skill.ninja;

import com.ww.game.member.MemberWisieManager;
import com.ww.game.member.command.MemberWisieAddStatusCommand;
import com.ww.game.play.command.skill.PlaySkillUnblockAllCommand;
import com.ww.game.play.flow.skill.PlaySkillFlow;
import com.ww.game.play.state.skill.PlaySkillOpponentState;
import com.ww.model.constant.wisie.MemberWisieStatus;
import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.war.WarTeam;

import java.util.HashMap;
import java.util.Map;

import static com.ww.game.play.modelfiller.PlayWarModelFiller.fillModelSkills;

public class PlaySkillKidnapFailedState extends PlaySkillOpponentState {
    public PlaySkillKidnapFailedState(PlaySkillFlow flow, MemberWisieManager manager, MemberWisieManager opponentManager) {
        super(flow, manager, opponentManager);
    }

    @Override
    public void initCommands() {
        commands.add(new MemberWisieAddStatusCommand(manager, MemberWisieStatus.KIDNAPPING_FAILED));
        commands.add(new MemberWisieAddStatusCommand(opponentManager, MemberWisieStatus.WAS_NOT_KIDNAPPED));
        commands.add(new PlaySkillUnblockAllCommand(opponentWarTeam));
    }

    @Override
    public Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam) {
        Map<String, Object> model = super.prepareModel(team, opponentTeam);
        fillModelSkills(model, opponentWarTeam, (WarTeam) team, (WarTeam) opponentTeam);
        return model;
    }

    @Override
    protected double minInterval() {
        return 2 - getWisie().getReflexF1() - getWisie().getConcentrationF1();
    }

    @Override
    protected double maxInterval() {
        return 4 - 2 * getWisie().getReflexF1() - 2 * getWisie().getConcentrationF1();
    }

    @Override
    public void after() {
        flow.run("REMOVING_NINJA");
    }
}
