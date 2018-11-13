package com.ww.game.play.state;

import com.ww.game.play.PlayManager;
import com.ww.game.play.command.PlayPrepareNextTaskCommand;
import com.ww.game.play.command.PlaySetDefaultTaskPropsCommand;
import com.ww.model.constant.Category;
import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.container.rival.RivalTeam;

import java.util.Map;

import static com.ww.game.play.modelfiller.PlayModelFiller.fillModelTaskMeta;

public class PlayRandomTaskPropsState extends PlayState {
    public PlayRandomTaskPropsState(PlayManager manager) {
        super(manager, RivalStatus.RANDOM_TASK_PROPS);
    }

    @Override
    public void initCommands() {
        commands.add(new PlayPrepareNextTaskCommand(getContainer(), Category.random(), DifficultyLevel.random(), getContainer().getInit().getCommonLanguage()));
    }

    @Override
    public Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam) {
        Map<String, Object> model = super.prepareModel(team, opponentTeam);
        fillModelTaskMeta(model, getContainer());
        return model;
    }

    @Override
    public long afterInterval() {
        return manager.getInterval().getRandomTaskPropsInterval();
    }

    @Override
    public void after() {
        manager.getFlow().run("PREPARING_NEXT_TASK");
    }

}
