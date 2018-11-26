package com.ww.game.play.command.skill.flow;

import com.ww.game.play.container.skill.PlayWarAnsweringFlowContainer;
import com.ww.game.play.flow.skill.PlaySkillWaterPistolFlow;

public class PlaySkillStartWaterPistolFlowCommand extends PlaySkillFlowOpponentCommand {

    public PlaySkillStartWaterPistolFlowCommand(PlayWarAnsweringFlowContainer flowContainer, Long creatorProfileId, Long opponentProfileId) {
        super(flowContainer, creatorProfileId, opponentProfileId);
    }

    @Override
    public void execute() {
        PlaySkillWaterPistolFlow flow = new PlaySkillWaterPistolFlow(flowContainer, creatorProfileId, opponentProfileId);
        flow.start();
    }
}
