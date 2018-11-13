package com.ww.game.play.state.war;

import com.ww.game.play.PlayManager;
import com.ww.game.play.command.PlayPrepareNextTaskCommand;
import com.ww.game.play.command.war.PlayWarSetActiveIndexCommand;
import com.ww.game.play.command.war.PlayWarStopActiveMemberManagerFlowsCommand;
import com.ww.game.play.state.PlayState;
import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.war.WarTeam;

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
        manager.getFlow().run("RANDOM_TASK_PROPS");
    }
}
