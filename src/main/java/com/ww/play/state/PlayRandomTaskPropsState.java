package com.ww.play.state;

import com.ww.model.constant.Category;
import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.container.rival.RivalTeam;
import com.ww.play.command.PlayPrepareNextTaskCommand;
import com.ww.play.command.PlaySetNextTimeoutCommand;
import com.ww.play.container.PlayContainer;

import java.util.Map;

import static com.ww.play.modelfiller.PlayModelFiller.fillModelNextInterval;
import static com.ww.play.modelfiller.PlayModelFiller.fillModelTaskMeta;

public class PlayRandomTaskPropsState extends PlayState {
    private long interval;

    public PlayRandomTaskPropsState(PlayContainer container, long interval) {
        super(container, RivalStatus.RANDOM_TASK_PROPS);
        this.interval = interval;
    }

    @Override
    public void initCommands() {
        commands.add(new PlayPrepareNextTaskCommand(container, Category.random(), DifficultyLevel.random(), container.getInit().getCommonLanguage()));
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
