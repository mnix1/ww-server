package com.ww.game.play.state.skill.changetask;

import com.ww.game.member.command.MemberWisieAddStatusCommand;
import com.ww.game.play.flow.PlayWarFlow;
import com.ww.game.play.flow.skill.PlaySkillFlow;
import com.ww.game.play.state.skill.PlaySkillState;
import com.ww.model.constant.wisie.MemberWisieStatus;

public class PlaySkillSubmitApplicationState extends PlaySkillState {

    public PlaySkillSubmitApplicationState(PlaySkillFlow flow) {
        super(flow);
    }

    @Override
    public void initCommands() {
        commands.add(new MemberWisieAddStatusCommand(manager, MemberWisieStatus.SUBMITS_APPLICATION));
    }

    @Override
    protected double minInterval() {
        return 3 - getWisie().getSpeedF1() - getWisie().getCunningF1() - getWisie().getConfidenceF1();
    }

    @Override
    protected double maxInterval() {
        return 5 - 2 * getWisie().getSpeedF1() - 2 * getWisie().getCunningF1() - 2 * getWisie().getConfidenceF1();
    }

    @Override
    protected double prepareInterval() {
        return hobbyImpact(super.prepareInterval());
    }

    @Override
    public void after() {
        new Thread(() -> ((PlayWarFlow) manager.getPlayManager().getFlow()).changeTask()).run();
    }
}
