package com.ww.game.play.state.skill.hint;

import com.ww.game.member.MemberWisieManager;
import com.ww.game.member.command.MemberWisieAddStatusCommand;
import com.ww.game.play.flow.skill.PlaySkillFlow;
import com.ww.game.play.state.skill.PlaySkillState;
import com.ww.model.constant.wisie.MemberWisieStatus;

public class PlaySkillWontUseHintState extends PlaySkillState {

    public PlaySkillWontUseHintState(PlaySkillFlow flow) {
        super(flow);
    }

    @Override
    public void initCommands() {
        commands.add(new MemberWisieAddStatusCommand(manager, MemberWisieStatus.WONT_USE_HINT));
    }

    @Override
    protected double prepareInterval() {
        return hobbyImpact(2 - getWisie().getReflexF1() - getWisie().getConcentrationF1());
    }

    @Override
    public void after() {
        flow.run("THINKING");
    }
}
