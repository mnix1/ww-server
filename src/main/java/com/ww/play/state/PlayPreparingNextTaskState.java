package com.ww.play.state;

import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.container.rival.RivalTeam;
import com.ww.play.command.PlaySetNextTimeoutCommand;
import com.ww.play.container.PlayContainer;

import java.util.Map;

import static com.ww.play.modelfiller.PlayModelFiller.fillModelNextInterval;
import static com.ww.play.modelfiller.PlayModelFiller.fillModelTaskMeta;

public class PlayPreparingNextTaskState extends PlayState {
    private long interval;

    public PlayPreparingNextTaskState(PlayContainer container, long interval) {
        super(container, RivalStatus.PREPARING_NEXT_TASK);
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
        fillModelTaskMeta(model, container);
        return model;
    }
}
