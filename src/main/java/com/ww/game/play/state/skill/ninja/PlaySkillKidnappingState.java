package com.ww.game.play.state.skill.ninja;

import com.ww.game.member.MemberWisieManager;
import com.ww.game.member.command.MemberWisieAddDisguiseCommand;
import com.ww.game.member.command.MemberWisieAddStatusCommand;
import com.ww.game.member.command.MemberWisieRunInnerFlowCommand;
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
import static com.ww.helper.RandomHelper.randomDouble;

public class PlaySkillKidnappingState extends PlaySkillOpponentState {
    private double skillValue;
    private double value;
    private double skillOpponentValue;
    private double opponentValue;
    private boolean success;

    public PlaySkillKidnappingState(PlaySkillFlow flow, MemberWisieManager manager, MemberWisieManager opponentManager) {
        super(flow, manager, opponentManager);
    }

    @Override
    public void initCommands() {
        commands.add(new MemberWisieAddStatusCommand(manager, MemberWisieStatus.TRYING_TO_KIDNAP));
        commands.add(new MemberWisieAddStatusCommand(opponentManager, MemberWisieStatus.TRYING_TO_DEFEND));
        commands.add(new MemberWisieRunInnerFlowCommand(opponentManager, flow));
        commands.add(new PlaySkillBlockAllCommand(manager.getContainer().getTeam()));
        commands.add(new PlaySkillBlockAllCommand(opponentManager.getContainer().getTeam()));
        commands.add(new MemberWisieAddDisguiseCommand(manager, DisguiseType.NINJA));
    }

    @Override
    public Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam) {
        Map<String, Object> model = super.prepareModel(team, opponentTeam);
        fillModelSkills(model, (WarTeam) team, (WarTeam) opponentTeam);
        fillModelActiveMemberAddOns(model, (WarTeam) team, (WarTeam) opponentTeam);
        return model;
    }

    @Override
    protected double minInterval() {
        return 1;
    }

    @Override
    protected double maxInterval() {
        return 3;
    }

    private void init() {
        skillValue = getWisie().getWisdomSum() + getWisie().getCunningF1();
        skillOpponentValue = getOpponentWisie().getWisdomSum() + getOpponentWisie().getCunningF1();
        value = randomDouble(skillValue, 2 * skillValue) + skillValue;
        opponentValue = randomDouble(skillOpponentValue, 2 * skillOpponentValue);
        success = value >= opponentValue;
    }

    @Override
    public void execute() {
        init();
        super.execute();
    }

    @Override
    public void after() {
        if (success) {
            flow.run("KIDNAP_SUCCEEDED");
        } else {
            flow.run("KIDNAP_FAILED");
            flow.run("WAS_NOT_KIDNAPPED");
        }
    }
}
