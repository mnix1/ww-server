package com.ww.game.play.state.war;

import com.ww.game.play.PlayManager;
import com.ww.game.play.command.PlayPrepareNextTaskCommand;
import com.ww.game.play.state.PlayRandomTaskPropsState;
import com.ww.game.play.state.PlayState;
import com.ww.model.constant.Category;
import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.container.rival.RivalTeam;

import java.util.Map;

import static com.ww.game.play.modelfiller.PlayModelFiller.fillModelTaskMeta;

public class PlayWarRandomTaskPropsState extends PlayRandomTaskPropsState {
    public PlayWarRandomTaskPropsState(PlayManager manager) {
        super(manager);
    }

    @Override
    public void after() {
        manager.getFlow().run("CHOOSING_WHO_ANSWER");
    }
}
