package com.ww.game.play.state;

import com.ww.game.play.PlayManager;
import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.model.constant.rival.RivalStatus;
import com.ww.game.play.command.PlayPrepareNextTaskCommand;
import com.ww.game.play.command.PlaySetTaskDifficultyCommand;
import com.ww.game.play.container.PlayContainer;

public class PlayChosenTaskDifficultyState extends PlayState {
    private DifficultyLevel difficultyLevel;

    public PlayChosenTaskDifficultyState(PlayManager manager, DifficultyLevel difficultyLevel) {
        super(manager, RivalStatus.CHOSEN_TASK_DIFFICULTY);
        this.difficultyLevel = difficultyLevel;
    }

    @Override
    public void initCommands() {
        commands.add(new PlaySetTaskDifficultyCommand(getContainer(), difficultyLevel));
        commands.add(new PlayPrepareNextTaskCommand(getContainer(),
                getContainer().getDecisions().getCategory(),
                difficultyLevel, getContainer().getInit().getCommonLanguage()));
    }

    @Override
    public void after() {
        manager.getFlow().run("PREPARING_NEXT_TASK");
    }
}
