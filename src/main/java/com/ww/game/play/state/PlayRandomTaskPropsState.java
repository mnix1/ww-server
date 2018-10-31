package com.ww.game.play.state;

import com.ww.model.constant.Category;
import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.container.rival.RivalTeam;
import com.ww.game.play.command.PlayPrepareNextTaskCommand;
import com.ww.game.play.container.PlayContainer;

import java.util.Map;

import static com.ww.game.play.modelfiller.PlayModelFiller.fillModelTaskMeta;

public class PlayRandomTaskPropsState extends PlayState {
    public PlayRandomTaskPropsState(PlayContainer container) {
        super(container, RivalStatus.RANDOM_TASK_PROPS);
    }

    @Override
    public void initCommands() {
        commands.add(new PlayPrepareNextTaskCommand(container, Category.random(), DifficultyLevel.random(), container.getInit().getCommonLanguage()));
    }

    @Override
    public Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam) {
        Map<String, Object> model = super.prepareModel(team, opponentTeam);
        fillModelTaskMeta(model, container);
        return model;
    }
}
