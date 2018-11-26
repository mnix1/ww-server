package com.ww.game.play.command.skill.flow;

import com.ww.game.play.container.skill.PlayWarAnsweringFlowContainer;

public abstract class PlaySkillFlowOpponentCommand extends PlaySkillFlowCommand {
    protected Long opponentProfileId;

    protected PlaySkillFlowOpponentCommand(PlayWarAnsweringFlowContainer flowContainer, Long creatorProfileId, Long opponentProfileId) {
        super(flowContainer, creatorProfileId);
        this.opponentProfileId = opponentProfileId;
    }
}
