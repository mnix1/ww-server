package com.ww.game.play.state;

import com.ww.game.play.PlayManager;
import com.ww.game.play.command.PlayPrepareNextTaskCommand;
import com.ww.model.constant.rival.RivalStatus;

public class PlayChoosingTaskPropsTimeoutState extends PlayState {
    public PlayChoosingTaskPropsTimeoutState(PlayManager manager) {
        super(manager, RivalStatus.CHOOSING_TASK_TIMEOUT);
    }

    @Override
    public void initCommands() {
        commands.add(new PlayPrepareNextTaskCommand(getContainer(),
                getContainer().getDecisions().getCategory(),
                getContainer().getDecisions().getDifficultyLevel(),
                getContainer().getInit().getCommonLanguage()));
    }

    @Override
    public void after() {
        manager.getFlow().run("PREPARING_NEXT_TASK");
    }
}
