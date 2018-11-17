package com.ww.game.play.state.skill.changetask;

import com.ww.game.member.MemberWisieManager;
import com.ww.game.member.command.MemberWisieAddDisguiseCommand;
import com.ww.game.member.command.MemberWisieAddStatusCommand;
import com.ww.game.play.flow.skill.PlaySkillFlow;
import com.ww.game.play.state.skill.PlaySkillState;
import com.ww.model.constant.wisie.DisguiseType;
import com.ww.model.constant.wisie.MemberWisieStatus;
import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.war.WarTeam;

import java.util.Map;

import static com.ww.game.play.modelfiller.PlayWarModelFiller.fillModelActiveMemberAddOns;
import static com.ww.game.play.modelfiller.PlayWarModelFiller.fillModelSkills;

public class PlaySkillWantChangeTaskState extends PlaySkillState {

    public PlaySkillWantChangeTaskState(PlaySkillFlow flow, MemberWisieManager manager) {
        super(flow, manager);
    }

    @Override
    public void initCommands() {
        commands.add(new MemberWisieAddStatusCommand(manager, MemberWisieStatus.WANTS_TO_CHANGE_TASK));
        commands.add(new MemberWisieAddDisguiseCommand(manager, DisguiseType.BIRD_RED));
    }

    @Override
    public Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam) {
        Map<String, Object> model = super.prepareModel(warTeam, team, opponentTeam);
        fillModelActiveMemberAddOns(model, warTeam, (WarTeam) team, (WarTeam) opponentTeam);
        fillModelSkills(model, (WarTeam) team, (WarTeam) opponentTeam, warTeam);
        return model;
    }

    @Override
    protected double minInterval() {
        return 2 - getWisie().getConfidenceF1() - getWisie().getCunningF1();
    }

    @Override
    protected double maxInterval() {
        return 4 - 2 * getWisie().getConfidenceF1() - 2 * getWisie().getCunningF1();
    }

    @Override
    protected double prepareInterval() {
        return hobbyImpact(super.prepareInterval());
    }

    @Override
    public void after() {
        flow.run("SUBMIT_APPLICATION");
    }
}
