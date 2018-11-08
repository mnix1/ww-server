package com.ww.game.play.state.skill.hint;

import com.ww.game.member.MemberWisieManager;
import com.ww.game.member.command.MemberWisieAddStatusCommand;
import com.ww.game.play.flow.skill.PlaySkillFlow;
import com.ww.game.play.state.skill.PlaySkillState;
import com.ww.model.constant.wisie.MemberWisieStatus;

public class PlaySkillThinkingState extends PlaySkillState {

    public PlaySkillThinkingState(PlaySkillFlow flow, MemberWisieManager manager) {
        super(flow, manager);
    }

    @Override
    public void initCommands() {
        commands.add(new MemberWisieAddStatusCommand(manager, MemberWisieStatus.THINKING));
    }

    @Override
    protected double minInterval() {
        double difficultyPart = (getContainer().getDifficulty() - 4) * 0.5;
        return 4 + difficultyPart - getWisie().getSpeedF1() - getWisie().getIntuitionF1() - getWisie().getWisdomSum();
    }

    @Override
    protected double maxInterval() {
        double difficultyPart = (getContainer().getDifficulty() - 4) * 0.5;
        return 8 + difficultyPart - 2 * getWisie().getSpeedF1() - 2 * getWisie().getIntuitionF1() - 2 * getWisie().getWisdomSum();
    }

    @Override
    protected double prepareInterval() {
        return hobbyImpact(super.prepareInterval());
    }

    @Override
    public void after() {
        flow.run("ANSWERING_WITHOUT_HINT");
    }
}
