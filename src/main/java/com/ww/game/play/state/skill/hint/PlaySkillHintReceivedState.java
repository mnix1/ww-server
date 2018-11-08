package com.ww.game.play.state.skill.hint;

import com.ww.game.member.MemberWisieManager;
import com.ww.game.member.command.MemberWisieAddStatusCommand;
import com.ww.game.play.flow.skill.PlaySkillFlow;
import com.ww.game.play.state.skill.PlaySkillState;
import com.ww.model.constant.wisie.MemberWisieStatus;

import static com.ww.helper.RandomHelper.randomDouble;

public class PlaySkillHintReceivedState extends PlaySkillState {

    public PlaySkillHintReceivedState(PlaySkillFlow flow, MemberWisieManager manager) {
        super(flow, manager);
    }

    @Override
    public void initCommands() {
        commands.add(new MemberWisieAddStatusCommand(manager, MemberWisieStatus.HINT_RECEIVED));
    }

    @Override
    protected double minInterval() {
        return 2 - getWisie().getReflexF1() - getWisie().getCunningF1();
    }

    @Override
    protected double maxInterval() {
        return 4 - 2 * getWisie().getReflexF1() - 2 * getWisie().getCunningF1();
    }

    @Override
    protected double prepareInterval() {
        return hobbyImpact(super.prepareInterval());
    }

    @Override
    public void after() {
        flow.run("THINKING_IF_USE_HINT");
    }
}
