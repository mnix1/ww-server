package com.ww.play.command;

import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.play.container.PlayContainer;

public class PlaySetTaskDifficultyCommand extends PlayCommand {
    private DifficultyLevel difficultyLevel;

    public PlaySetTaskDifficultyCommand(PlayContainer container, DifficultyLevel difficultyLevel) {
        super(container);
        this.difficultyLevel = difficultyLevel;
    }

    @Override
    public void execute() {
        container.getDecisions().chosenDifficulty(difficultyLevel);
    }
}
