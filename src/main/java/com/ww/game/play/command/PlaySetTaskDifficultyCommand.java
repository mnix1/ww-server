package com.ww.game.play.command;

import com.ww.game.GameCommand;
import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.game.play.container.PlayContainer;

public class PlaySetTaskDifficultyCommand extends GameCommand {
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
