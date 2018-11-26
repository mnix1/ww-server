package com.ww.game.play.flow.skill;

import com.ww.game.play.container.skill.PlayWarAnsweringFlowContainer;
import com.ww.game.play.state.skill.waterpistol.PlaySkillCleanedState;
import com.ww.game.play.state.skill.waterpistol.PlaySkillCleaningState;
import com.ww.game.play.state.skill.waterpistol.PlaySkillWaterPistolUsedState;

public class PlaySkillWaterPistolFlow extends PlaySkillFlowOpponent {

    public PlaySkillWaterPistolFlow(PlayWarAnsweringFlowContainer flowContainer, Long creatorProfileId, Long opponentProfileId) {
        super(flowContainer, creatorProfileId, opponentProfileId);
    }

    @Override
    protected void initStateMap() {
        stateMap.put("WATER_PISTOL_USED", () -> new PlaySkillWaterPistolUsedState(this));
        stateMap.put("CLEANING", () -> new PlaySkillCleaningState(this));
        stateMap.put("CLEANED", () -> new PlaySkillCleanedState(this));
    }

    @Override
    public void start() {
        super.start();
        run("WATER_PISTOL_USED");
    }
}
