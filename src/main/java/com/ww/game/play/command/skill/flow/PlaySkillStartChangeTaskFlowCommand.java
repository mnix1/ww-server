package com.ww.game.play.command.skill.flow;

import com.ww.game.play.container.skill.PlayWarAnsweringFlowContainer;
import com.ww.game.play.flow.skill.PlaySkillChangeTaskFlow;

public class PlaySkillStartChangeTaskFlowCommand extends PlaySkillFlowCommand {
    public PlaySkillStartChangeTaskFlowCommand(PlayWarAnsweringFlowContainer flowContainer, Long creatorProfileId) {
        super(flowContainer, creatorProfileId);
    }

    @Override
    public void execute() {
        PlaySkillChangeTaskFlow flow = new PlaySkillChangeTaskFlow(flowContainer, creatorProfileId);
        flow.start();
    }
}
