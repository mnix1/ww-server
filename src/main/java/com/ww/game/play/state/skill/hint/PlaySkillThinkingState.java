package com.ww.game.play.state.skill.hint;

import com.ww.game.member.MemberWisieManager;
import com.ww.game.member.command.MemberWisieAddStatusCommand;
import com.ww.game.play.flow.skill.PlaySkillFlow;
import com.ww.game.play.state.skill.PlaySkillState;
import com.ww.model.constant.wisie.MemberWisieStatus;

public class PlaySkillThinkingState extends PlaySkillState {

    public PlaySkillThinkingState(PlaySkillFlow flow) {
        super(flow);
    }

    @Override
    public void initCommands() {
        commands.add(new MemberWisieAddStatusCommand(manager, MemberWisieStatus.THINKING));
    }

    @Override
    protected double minInterval() {
        double difficultyPart = manager.getContainer().getDifficulty() * (1 - Math.max(getWisie().getWisdomSum(), getWisie().getConfidenceF1()));
        return 1 - getWisie().getWisdomSum() + difficultyPart;
    }

    @Override
    protected double maxInterval() {
        double difficultyPart = manager.getContainer().getDifficulty() * (1 - Math.max(getWisie().getWisdomSum(), getWisie().getConfidenceF1()));
        return 2 - getWisie().getWisdomSum() - getWisie().getConfidenceF1() + difficultyPart;
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
