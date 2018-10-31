package com.ww.game.play.state.war;

import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.war.WarTeam;
import com.ww.game.play.command.PlaySetNextTimeoutCommand;
import com.ww.game.play.command.war.PlayWarSetDefaultActiveIndexCommand;
import com.ww.game.play.container.PlayContainer;
import com.ww.game.play.state.PlayState;

import java.util.Map;

import static com.ww.game.play.modelfiller.PlayModelFiller.fillModelNextInterval;
import static com.ww.game.play.modelfiller.PlayModelFiller.fillModelTaskMeta;
import static com.ww.game.play.modelfiller.PlayWarModelFiller.fillModelActiveIndexes;
import static com.ww.game.play.modelfiller.PlayWarModelFiller.fillModelIsChosenActiveIndex;

public class PlayWarChoosingWhoAnswerState extends PlayState {
    private long interval;

    public PlayWarChoosingWhoAnswerState(PlayContainer container, long interval) {
        super(container, RivalStatus.CHOOSING_WHO_ANSWER);
        this.interval = interval;
    }

    @Override
    public void initCommands() {
        commands.add(new PlaySetNextTimeoutCommand(container, interval));
        initSetDefaultActiveIndexCommand();
    }

    protected void initSetDefaultActiveIndexCommand() {
        commands.add(new PlayWarSetDefaultActiveIndexCommand(container));
    }

    @Override
    public Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam) {
        Map<String, Object> model = super.prepareModel(team, opponentTeam);
        fillModelNextInterval(model, container);
        fillModelTaskMeta(model, container);
        fillModelActiveIndexes(model, (WarTeam) team, (WarTeam) opponentTeam);
        fillModelIsChosenActiveIndex(model, (WarTeam) team);
        return model;
    }
}
