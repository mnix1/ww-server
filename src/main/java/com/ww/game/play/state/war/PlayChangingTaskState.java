package com.ww.game.play.state.war;

import com.ww.game.play.PlayManager;
import com.ww.game.play.command.war.PlayWarStopActiveMemberManagerFlowsCommand;
import com.ww.game.play.state.PlayState;
import com.ww.model.constant.rival.RivalStatus;

public class PlayChangingTaskState extends PlayState {
    public PlayChangingTaskState(PlayManager manager) {
        super(manager, RivalStatus.CHANGING_TASK);
    }

    @Override
    public void initCommands() {
        commands.add(new PlayWarStopActiveMemberManagerFlowsCommand(getContainer()));
    }

    @Override
    public long afterInterval() {
        return manager.getInterval().getChangingTaskInterval();
    }

    @Override
    public void after() {
        String stateName = "RANDOM_TASK_PROPS";
        if (getContainer().isEnd()) {
            stateName = "END";
        }
        manager.getFlow().run(stateName);
    }
}
