package com.ww.game.play.command.skill.flow;

import com.ww.game.GameCommand;
import com.ww.game.play.container.skill.PlayWarAnsweringFlowContainer;

public abstract class PlaySkillFlowCommand extends GameCommand {
    protected PlayWarAnsweringFlowContainer flowContainer;
    protected Long creatorProfileId;

    protected PlaySkillFlowCommand(PlayWarAnsweringFlowContainer flowContainer, Long creatorProfileId) {
        this.flowContainer = flowContainer;
        this.creatorProfileId = creatorProfileId;
    }
}
