package com.ww.play.state;

import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.container.rival.RivalTeam;
import com.ww.play.command.PlaySetNextTimeoutCommand;
import com.ww.play.container.PlayContainer;

import java.util.Map;

import static com.ww.play.modelfiller.PlayModelFiller.fillModelChoosingTaskPropsTag;
import static com.ww.play.modelfiller.PlayModelFiller.fillModelNextInterval;
import static com.ww.play.modelfiller.PlayModelFiller.fillModelSimpleNextTaskMeta;

public class PlayChoosingTaskPropsState extends PlayState {
    private long interval;

    public PlayChoosingTaskPropsState(PlayContainer container, long interval) {
        super(container, RivalStatus.CHOOSING_TASK_PROPS);
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
        fillModelSimpleNextTaskMeta(model, container);
        fillModelChoosingTaskPropsTag(model, container);
        return model;
    }
}
