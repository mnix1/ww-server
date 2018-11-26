package com.ww.game.play.state.skill.waterpistol;

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

public class PlaySkillWaterPistolUsedState extends PlaySkillOpponentState {

    public PlaySkillWaterPistolUsedState(PlaySkillFlowOpponent flow) {
        super(flow);
    }

    @Override
    public void initCommands() {
        commands.add(new MemberWisieRunInnerFlowCommand(flow, opponentWarTeam.getProfileId()));
        commands.add(new MemberWisieAddStatusCommand(opponentManager, MemberWisieStatus.WATER_PISTOL_USED_ON_IT));
        commands.add(new MemberWisieAddDisguiseCommand(opponentManager, DisguiseType.PENGUIN_RAIN));
        commands.add(new PlaySkillBlockAllCommand(opponentWarTeam));
    }

    @Override
    public Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam) {
        Map<String, Object> model = super.prepareModel(team, opponentTeam);
        fillModelSkills(model, (WarTeam) team, (WarTeam) opponentTeam);
        fillModelActiveMemberAddOns(model, opponentWarTeam, (WarTeam) team, (WarTeam) opponentTeam);
        return model;
    }

    @Override
    protected double minInterval() {
        return 8 - 6 * getWisie().getConcentrationF1();
    }

    @Override
    protected double maxInterval() {
        return 16 - 12 * getWisie().getConcentrationF1();
    }

    @Override
    public void after() {
        flow.run("CLEANING");
    }
}
