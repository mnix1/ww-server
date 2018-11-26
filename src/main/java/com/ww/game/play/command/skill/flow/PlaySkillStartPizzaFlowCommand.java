package com.ww.game.play.command.skill.flow;

import com.ww.game.play.container.skill.PlayWarAnsweringFlowContainer;
import com.ww.game.play.flow.skill.PlaySkillPizzaFlow;

public class PlaySkillStartPizzaFlowCommand extends PlaySkillFlowOpponentCommand {
    public PlaySkillStartPizzaFlowCommand(PlayWarAnsweringFlowContainer flowContainer, Long creatorProfileId, Long opponentProfileId) {
        super(flowContainer, creatorProfileId, opponentProfileId);
    }

    @Override
    public void execute() {
        PlaySkillPizzaFlow flow = new PlaySkillPizzaFlow(flowContainer, creatorProfileId, opponentProfileId);
        flow.start();
    }
}
