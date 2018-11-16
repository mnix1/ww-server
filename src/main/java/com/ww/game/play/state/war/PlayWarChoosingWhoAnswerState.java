package com.ww.game.play.state.war;

import com.ww.game.play.PlayManager;
import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.war.WarInterval;
import com.ww.model.container.rival.war.WarTeam;
import com.ww.game.play.command.PlaySetNextTimeoutCommand;
import com.ww.game.play.command.war.PlayWarSetDefaultActiveIndexCommand;
import com.ww.game.play.state.PlayState;

import java.util.Map;

import static com.ww.game.play.modelfiller.PlayModelFiller.fillModelNextTimeout;
import static com.ww.game.play.modelfiller.PlayModelFiller.fillModelTaskMeta;
import static com.ww.game.play.modelfiller.PlayWarModelFiller.fillModelActiveIndexes;
import static com.ww.game.play.modelfiller.PlayWarModelFiller.fillModelIsChosenActiveIndex;

public class PlayWarChoosingWhoAnswerState extends PlayState {

    public PlayWarChoosingWhoAnswerState(PlayManager manager) {
        super(manager, RivalStatus.CHOOSING_WHO_ANSWER);
    }

    @Override
    public void initCommands() {
        commands.add(new PlaySetNextTimeoutCommand(getContainer(), ((WarInterval) manager.getInterval()).getChoosingWhoAnswerInterval()));
        initSetDefaultActiveIndexCommand();
    }

    protected void initSetDefaultActiveIndexCommand() {
        commands.add(new PlayWarSetDefaultActiveIndexCommand(getContainer()));
    }

    @Override
    public Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam) {
        Map<String, Object> model = super.prepareModel(team, opponentTeam);
        fillModelNextTimeout(model, getContainer());
        fillModelTaskMeta(model, getContainer());
        fillModelActiveIndexes(model, (WarTeam) team, (WarTeam) opponentTeam);
        fillModelIsChosenActiveIndex(model, (WarTeam) team);
        return model;
    }

    @Override
    public long afterInterval() {
        return ((WarInterval) manager.getInterval()).getChoosingWhoAnswerInterval();
    }

    @Override
    public void after() {
        manager.getFlow().run("PREPARING_NEXT_TASK");
    }
}
