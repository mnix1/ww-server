package com.ww.play.state;

import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.model.constant.rival.RivalStatus;
import com.ww.play.command.PlayPrepareNextTaskCommand;
import com.ww.play.command.PlaySetTaskDifficultyCommand;
import com.ww.play.container.PlayContainer;

public class PlayChosenTaskDifficultyState extends PlayState {
    private DifficultyLevel difficultyLevel;

    public PlayChosenTaskDifficultyState(PlayContainer container, DifficultyLevel difficultyLevel) {
        super(container, RivalStatus.CHOSEN_TASK_DIFFICULTY);
        this.difficultyLevel = difficultyLevel;
    }

    @Override
    public void initCommands() {
        commands.add(new PlaySetTaskDifficultyCommand(container, difficultyLevel));
        commands.add(new PlayPrepareNextTaskCommand(container, container.getDecisions().getCategory(), difficultyLevel, container.getInit().getCommonLanguage()));
    }
}
