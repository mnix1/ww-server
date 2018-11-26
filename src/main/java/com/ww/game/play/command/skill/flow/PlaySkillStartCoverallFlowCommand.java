package com.ww.game.play.command.skill.flow;

import com.ww.game.play.container.skill.PlayWarAnsweringFlowContainer;
import com.ww.game.play.flow.skill.PlaySkillCoverallFlow;

public class PlaySkillStartCoverallFlowCommand extends PlaySkillFlowOpponentCommand {

    public PlaySkillStartCoverallFlowCommand(PlayWarAnsweringFlowContainer flowContainer, Long creatorProfileId, Long opponentProfileId) {
        super(flowContainer, creatorProfileId, opponentProfileId);
    }

    @Override
    public void execute() {
        PlaySkillCoverallFlow flow = new PlaySkillCoverallFlow(flowContainer, creatorProfileId, opponentProfileId);
        flow.start();
    }
}
