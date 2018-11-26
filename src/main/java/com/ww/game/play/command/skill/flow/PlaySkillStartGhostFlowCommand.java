package com.ww.game.play.command.skill.flow;

import com.ww.game.play.container.skill.PlayWarAnsweringFlowContainer;
import com.ww.game.play.flow.skill.PlaySkillCoverallFlow;
import com.ww.game.play.flow.skill.PlaySkillGhostFlow;

public class PlaySkillStartGhostFlowCommand extends PlaySkillFlowOpponentCommand {
    public PlaySkillStartGhostFlowCommand(PlayWarAnsweringFlowContainer flowContainer, Long creatorProfileId, Long opponentProfileId) {
        super(flowContainer, creatorProfileId, opponentProfileId);
    }

    @Override
    public void execute() {
        PlaySkillGhostFlow flow = new PlaySkillGhostFlow(flowContainer, creatorProfileId, opponentProfileId);
        flow.start();
    }
}
