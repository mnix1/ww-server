package com.ww.game.play.state.skill.hint;

import com.ww.game.member.MemberWisieManager;
import com.ww.game.member.command.MemberWisieAddStatusCommand;
import com.ww.game.play.flow.skill.PlaySkillFlow;
import com.ww.game.play.state.skill.PlaySkillState;
import com.ww.model.constant.wisie.MemberWisieStatus;

public class PlaySkillWontUseHintState extends PlaySkillState {

    public PlaySkillWontUseHintState(PlaySkillFlow flow, MemberWisieManager manager) {
        super(flow, manager);
    }

    @Override
    public void initCommands() {
        commands.add(new MemberWisieAddStatusCommand(manager, MemberWisieStatus.WONT_USE_HINT));
    }

    @Override
    protected double minInterval() {
        return 2 - getWisie().getSpeedF1() - getWisie().getWisdomSum();
    }

    @Override
    protected double maxInterval() {
        return 4 - 2 * getWisie().getSpeedF1() - 2 * getWisie().getWisdomSum();
    }

    @Override
    protected double prepareInterval() {
        return hobbyImpact(super.prepareInterval());
    }

    @Override
    public void after() {
        flow.run("THINKING");
    }
}
