package com.ww.game.play.state;

import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.container.rival.RivalTeam;
import com.ww.game.play.command.PlaySetDefaultTaskPropsCommand;
import com.ww.game.play.command.PlaySetNextTimeoutCommand;
import com.ww.game.play.container.PlayContainer;

import java.util.Map;

import static com.ww.game.play.modelfiller.PlayModelFiller.*;

public class PlayChoosingTaskCategoryState extends PlayState {
    private long interval;

    public PlayChoosingTaskCategoryState(PlayContainer container, long interval) {
        super(container, RivalStatus.CHOOSING_TASK_CATEGORY);
        this.interval = interval;
    }

    @Override
    public void initCommands() {
        commands.add(new PlaySetDefaultTaskPropsCommand(container));
        commands.add(new PlaySetNextTimeoutCommand(container, interval));
    }

    @Override
    public Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam) {
        Map<String, Object> model = super.prepareModel(team, opponentTeam);
        fillModelNextInterval(model, container);
        fillModelSimpleNextTaskMeta(model, container);
        fillModelChoosingTaskPropsTag(model, container);
        fillModelChosenCategory(model, container);
        return model;
    }
}
