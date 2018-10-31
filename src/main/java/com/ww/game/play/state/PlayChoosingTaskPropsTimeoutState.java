package com.ww.game.play.state;

import com.ww.model.constant.rival.RivalStatus;
import com.ww.game.play.command.PlayPrepareNextTaskCommand;
import com.ww.game.play.command.PlaySetTaskDifficultyCommand;
import com.ww.game.play.container.PlayContainer;

public class PlayChoosingTaskPropsTimeoutState extends PlayState {
    public PlayChoosingTaskPropsTimeoutState(PlayContainer container) {
        super(container, RivalStatus.CHOOSING_TASK_TIMEOUT);
    }

    @Override
    public void initCommands() {
        commands.add(new PlayPrepareNextTaskCommand(container, container.getDecisions().getCategory(), container.getDecisions().getDifficultyLevel(), container.getInit().getCommonLanguage()));
    }
}
