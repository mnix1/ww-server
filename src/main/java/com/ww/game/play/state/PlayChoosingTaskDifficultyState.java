package com.ww.game.play.state;

import com.ww.game.play.PlayManager;
import com.ww.game.play.command.PlaySetNextTimeoutCommand;
import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.container.rival.RivalTeam;

import java.util.Map;

import static com.ww.game.play.modelfiller.PlayModelFiller.fillModelChosenDifficulty;
import static com.ww.game.play.modelfiller.PlayModelFiller.fillModelNextInterval;

public class PlayChoosingTaskDifficultyState extends PlayState {

    public PlayChoosingTaskDifficultyState(PlayManager manager) {
        super(manager, RivalStatus.CHOOSING_TASK_DIFFICULTY);
    }

    @Override
    public void initCommands() {
        commands.add(new PlaySetNextTimeoutCommand(getContainer(), manager.getInterval().getChoosingTaskDifficultyInterval()));
    }

    @Override
    public Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam) {
        Map<String, Object> model = super.prepareModel(team, opponentTeam);
        fillModelNextInterval(model, getContainer());
        fillModelChosenDifficulty(model, getContainer());
        return model;
    }

    @Override
    public long afterInterval() {
        return manager.getInterval().getChoosingTaskDifficultyInterval();
    }

    @Override
    public void after() {
        manager.getFlow().run("CHOOSING_TASK_PROPS_TIMEOUT");
    }
}
