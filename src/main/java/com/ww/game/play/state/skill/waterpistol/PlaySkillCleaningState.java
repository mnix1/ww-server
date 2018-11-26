package com.ww.game.play.state.skill.waterpistol;

import com.ww.game.member.command.MemberWisieAddStatusCommand;
import com.ww.game.play.flow.skill.PlaySkillFlowOpponent;
import com.ww.game.play.state.skill.PlaySkillOpponentState;
import com.ww.model.constant.wisie.MemberWisieStatus;

public class PlaySkillCleaningState extends PlaySkillOpponentState {

    public PlaySkillCleaningState(PlaySkillFlowOpponent flow) {
        super(flow);
    }

    @Override
    public void initCommands() {
        commands.add(new MemberWisieAddStatusCommand(opponentManager, MemberWisieStatus.CLEANING));
    }

    @Override
    protected double minInterval() {
        return 8 - 4 * getWisie().getSpeedF1() - 4 * getWisie().getCunningF1();
    }

    @Override
    protected double maxInterval() {
        return 16 - 8 * getWisie().getSpeedF1() - 8 * getWisie().getCunningF1();
    }

    @Override
    public void after() {
        flow.run("CLEANED");
    }
}
