package com.ww.game.play.command.skill.flow;

import com.ww.game.play.container.skill.PlayWarAnsweringFlowContainer;
import com.ww.game.play.flow.skill.PlaySkillNinjaFlow;

public class PlaySkillStartNinjaFlowCommand extends PlaySkillFlowOpponentCommand {
    public PlaySkillStartNinjaFlowCommand(PlayWarAnsweringFlowContainer flowContainer, Long creatorProfileId, Long opponentProfileId) {
        super(flowContainer, creatorProfileId, opponentProfileId);
    }

    @Override
    public void execute() {
        PlaySkillNinjaFlow flow = new PlaySkillNinjaFlow(flowContainer, creatorProfileId, opponentProfileId);
        flow.start();
    }
}
