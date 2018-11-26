package com.ww.game.play.state.skill.hint;

import com.ww.game.member.command.MemberWisieAddStatusCommand;
import com.ww.game.member.command.MemberWisieRunInnerFlowCommand;
import com.ww.game.play.command.skill.PlaySkillBlockAllCommand;
import com.ww.game.play.flow.skill.PlaySkillFlow;
import com.ww.game.play.state.skill.PlaySkillState;
import com.ww.model.constant.wisie.MemberWisieStatus;
import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.war.WarTeam;

import java.util.Map;

import static com.ww.game.play.modelfiller.PlayWarModelFiller.fillModelSkills;

public class PlaySkillHintReceivedState extends PlaySkillState {

    public PlaySkillHintReceivedState(PlaySkillFlow flow) {
        super(flow);
    }

    @Override
    public void initCommands() {
        commands.add(new MemberWisieAddStatusCommand(manager, MemberWisieStatus.HINT_RECEIVED));
        commands.add(new MemberWisieRunInnerFlowCommand(flow, warTeam.getProfileId()));
        commands.add(new PlaySkillBlockAllCommand(warTeam));
    }

    @Override
    public Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam) {
        Map<String, Object> model = super.prepareModel(warTeam, team, opponentTeam);
        fillModelSkills(model, warTeam,(WarTeam) team, (WarTeam) opponentTeam);
        return model;
    }

    @Override
    protected double prepareInterval() {
        return hobbyImpact(3 - getWisie().getReflexF1() - getWisie().getConcentrationF1() - getWisie().getCunningF1());
    }

    @Override
    public void after() {
        flow.run("THINKING_IF_USE_HINT");
    }
}
