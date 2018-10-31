package com.ww.game.play.state;

import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.container.rival.RivalTeam;
import com.ww.game.play.command.PlaySetNextTimeoutCommand;
import com.ww.game.play.container.PlayContainer;

import java.util.Map;

import static com.ww.game.play.modelfiller.PlayModelFiller.fillModelNextInterval;
import static com.ww.game.play.modelfiller.PlayModelFiller.fillModelTask;

public class PlayAnsweringState extends PlayState {
    private long interval;

    public PlayAnsweringState(PlayContainer container, long interval) {
        super(container, RivalStatus.ANSWERING);
        this.interval = interval;
    }

    @Override
    public void initCommands() {
        commands.add(new PlaySetNextTimeoutCommand(container, interval));
    }

    @Override
    public Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam) {
        Map<String, Object> model = super.prepareModel(team, opponentTeam);
        fillModelNextInterval(model, container);
        fillModelTask(model, container);
        return model;
    }
}
