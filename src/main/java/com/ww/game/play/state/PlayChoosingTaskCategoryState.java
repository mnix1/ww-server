package com.ww.game.play.state;

import com.ww.game.play.PlayManager;
import com.ww.game.play.command.PlaySetDefaultTaskPropsCommand;
import com.ww.game.play.command.PlaySetNextTimeoutCommand;
import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.container.rival.RivalTeam;

import java.util.Map;

import static com.ww.game.play.modelfiller.PlayModelFiller.*;

public class PlayChoosingTaskCategoryState extends PlayState {
    public PlayChoosingTaskCategoryState(PlayManager manager) {
        super(manager, RivalStatus.CHOOSING_TASK_CATEGORY);
    }

    @Override
    public void initCommands() {
        commands.add(new PlaySetDefaultTaskPropsCommand(getContainer()));
        commands.add(new PlaySetNextTimeoutCommand(getContainer(), getManager().getInterval().getChoosingTaskCategoryInterval()));
    }

    @Override
    public Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam) {
        Map<String, Object> model = super.prepareModel(team, opponentTeam);
        fillModelNextInterval(model, getContainer());
        fillModelSimpleNextTaskMeta(model, getContainer());
        fillModelChoosingTaskPropsTag(model, getContainer());
        fillModelChosenCategory(model, getContainer());
        return model;
    }

    @Override
    public long afterInterval() {
        return manager.getInterval().getChoosingTaskCategoryInterval();
    }

    @Override
    public void after() {
        manager.getFlow().run("CHOOSING_TASK_PROPS_TIMEOUT");
    }
}
