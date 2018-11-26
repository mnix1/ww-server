package com.ww.game.play.command.skill.flow;

import com.ww.game.play.container.skill.PlayWarAnsweringFlowContainer;
import com.ww.game.play.flow.skill.PlaySkillHintFlow;

public class PlaySkillStartHintFlowCommand extends PlaySkillFlowCommand {
    private Long answerId;

    public PlaySkillStartHintFlowCommand(PlayWarAnsweringFlowContainer flowContainer, Long creatorProfileId, Long answerId) {
        super(flowContainer, creatorProfileId);
        this.answerId = answerId;
    }

    @Override
    public void execute() {
        PlaySkillHintFlow flow = new PlaySkillHintFlow(flowContainer, creatorProfileId, answerId);
        flow.start();
    }
}
