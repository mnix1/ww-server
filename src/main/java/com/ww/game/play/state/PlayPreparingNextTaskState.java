package com.ww.game.play.state;

import com.ww.game.play.PlayManager;
import com.ww.game.play.command.PlaySetNextTimeoutCommand;
import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.container.rival.RivalTeam;

import java.util.Map;

import static com.ww.game.play.modelfiller.PlayModelFiller.*;

public class PlayPreparingNextTaskState extends PlayState {
    public PlayPreparingNextTaskState(PlayManager manager) {
        super(manager, RivalStatus.PREPARING_NEXT_TASK);
    }

    @Override
    public void initCommands() {
        commands.add(new PlaySetNextTimeoutCommand(getContainer(), manager.getInterval().getPreparingNextTaskInterval()));
    }

    @Override
    public Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam) {
        Map<String, Object> model = super.prepareModel(team, opponentTeam);
        fillModelNextInterval(model, getContainer());
        fillModelTaskMeta(model, getContainer());
        fillModelNullAnswered(model);
        return model;
    }

    @Override
    public long afterInterval() {
        return manager.getInterval().getPreparingNextTaskInterval();
    }

    @Override
    public void after(){
        manager.getFlow().run("ANSWERING");
    }
}
