package com.ww.game.play.state.skill.coverall;

import com.ww.game.member.MemberWisieManager;
import com.ww.game.member.command.MemberWisieAddDisguiseCommand;
import com.ww.game.member.command.MemberWisieAddStatusCommand;
import com.ww.game.member.command.MemberWisieIncreaseAttributesCommand;
import com.ww.game.member.command.MemberWisieMaybeRunOuterFlowCommand;
import com.ww.game.play.command.skill.PlaySkillBlockAllCommand;
import com.ww.game.play.flow.skill.PlaySkillFlow;
import com.ww.game.play.state.skill.PlaySkillOpponentState;
import com.ww.model.constant.wisie.DisguiseType;
import com.ww.model.constant.wisie.MemberWisieStatus;
import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.war.WarTeam;

import java.util.Map;

import static com.ww.game.play.modelfiller.PlayWarModelFiller.fillModelActiveMemberAddOns;
import static com.ww.game.play.modelfiller.PlayWarModelFiller.fillModelSkills;
import static com.ww.game.play.modelfiller.PlayWarModelFiller.fillModelTeams;

public class PlaySkillCoverallState extends PlaySkillOpponentState {
    public PlaySkillCoverallState(PlaySkillFlow flow, MemberWisieManager manager, MemberWisieManager opponentManager) {
        super(flow, manager, opponentManager);
    }

    @Override
    public void initCommands() {
        commands.add(new MemberWisieAddStatusCommand(manager, MemberWisieStatus.COVERALL_READY));
        commands.add(new PlaySkillBlockAllCommand(opponentWarTeam));
        commands.add(new MemberWisieAddDisguiseCommand(manager, DisguiseType.COVERALL));
        commands.add(new MemberWisieIncreaseAttributesCommand(manager));
        commands.add(new MemberWisieMaybeRunOuterFlowCommand(opponentManager));
    }

    @Override
    public Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam) {
        Map<String, Object> model = super.prepareModel(warTeam, team, opponentTeam);
        fillModelSkills(model, (WarTeam) team, (WarTeam) opponentTeam);
        fillModelActiveMemberAddOns(model, warTeam, (WarTeam) team, (WarTeam) opponentTeam);
        fillModelTeams(model, warTeam, (WarTeam) team, (WarTeam) opponentTeam);
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
        flow.notifyOuter(manager.getFlow(), true);
    }
}
