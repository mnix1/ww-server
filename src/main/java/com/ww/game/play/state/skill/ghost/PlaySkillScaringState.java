package com.ww.game.play.state.skill.ghost;

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

public class PlaySkillScaringState extends PlaySkillOpponentState {
    private double skillValue;
    private double value;
    private double skillOpponentValue;
    private double opponentValue;
    private boolean success;

    public PlaySkillScaringState(PlaySkillFlow flow, MemberWisieManager manager, MemberWisieManager opponentManager) {
        super(flow, manager, opponentManager);
    }

    @Override
    public void initCommands() {
        commands.add(new MemberWisieAddStatusCommand(manager, MemberWisieStatus.TRYING_TO_SCARE));
        commands.add(new MemberWisieRunInnerFlowCommand(opponentManager, flow));
        commands.add(new MemberWisieAddStatusCommand(opponentManager, MemberWisieStatus.SCARING_ON_IT));
        commands.add(new PlaySkillBlockAllCommand(warTeam));
        commands.add(new PlaySkillBlockAllCommand(opponentWarTeam));
        commands.add(new MemberWisieAddDisguiseCommand(manager, DisguiseType.GHOST));
    }

    @Override
    public Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam) {
        Map<String, Object> model = super.prepareModel(team, opponentTeam);
        fillModelSkills(model, (WarTeam) team, (WarTeam) opponentTeam);
        fillModelActiveMemberAddOns(model, warTeam, (WarTeam) team, (WarTeam) opponentTeam);
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

    private void init() {
        skillValue = getWisie().getIntuitionF1() + getWisie().getConfidenceF1() + getWisie().getCunningF1();
        skillOpponentValue = getOpponentWisie().getIntuitionF1() + getOpponentWisie().getConfidenceF1() + getOpponentWisie().getCunningF1();
        value = randomDouble(skillValue, 2 * skillValue) + 0.5 * skillValue;
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
            flow.run("SCARE_SUCCEEDED");
        } else {
            flow.run("SCARE_FAILED");
        }
    }
}
