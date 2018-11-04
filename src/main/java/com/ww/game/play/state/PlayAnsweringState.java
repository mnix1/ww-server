package com.ww.game.play.state;

import com.ww.game.play.PlayManager;
import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.container.rival.RivalTeam;
import com.ww.game.play.command.PlaySetNextTimeoutCommand;
import com.ww.game.play.container.PlayContainer;

import java.util.Map;

import static com.ww.game.play.modelfiller.PlayModelFiller.fillModelNextInterval;
import static com.ww.game.play.modelfiller.PlayModelFiller.fillModelTask;

public class PlayAnsweringState extends PlayState {
    public PlayAnsweringState(PlayManager manager) {
        super(manager, RivalStatus.ANSWERING);
    }

    @Override
    public void initCommands() {
        commands.add(new PlaySetNextTimeoutCommand(getContainer(), manager.getInterval().getAnsweringInterval()));
    }

    @Override
    public Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam) {
        Map<String, Object> model = super.prepareModel(team, opponentTeam);
        fillModelNextInterval(model, getContainer());
        fillModelTask(model, getContainer());
        return model;
    }

    @Override
    public long afterInterval() {
        return manager.getInterval().getAnsweringInterval();
    }

    @Override
    public void after() {
        manager.getFlow().run("ANSWERING_TIMEOUT");
    }
}
