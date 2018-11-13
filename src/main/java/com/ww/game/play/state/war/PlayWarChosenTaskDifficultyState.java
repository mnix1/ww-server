package com.ww.game.play.state.war;

import com.ww.game.play.PlayManager;
import com.ww.game.play.state.PlayChosenTaskDifficultyState;
import com.ww.model.constant.rival.DifficultyLevel;

public class PlayWarChosenTaskDifficultyState extends PlayChosenTaskDifficultyState {

    public PlayWarChosenTaskDifficultyState(PlayManager manager, DifficultyLevel difficultyLevel) {
        super(manager, difficultyLevel);
    }

    @Override
    public void after() {
        manager.getFlow().run("CHOOSING_WHO_ANSWER");
    }
}
