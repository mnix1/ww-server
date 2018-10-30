package com.ww.play.state.war;

import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.war.WarTeam;
import com.ww.play.command.PlaySetNextTimeoutCommand;
import com.ww.play.command.war.PlaySetDefaultActiveIndexCommand;
import com.ww.play.container.PlayContainer;
import com.ww.play.state.PlayState;

import java.util.Map;

import static com.ww.play.modelfiller.PlayModelFiller.fillModelNextInterval;
import static com.ww.play.modelfiller.PlayModelFiller.fillModelTaskMeta;
import static com.ww.play.modelfiller.PlayWarModelFiller.fillModelActiveIndexes;
import static com.ww.play.modelfiller.PlayWarModelFiller.fillModelIsChosenActiveIndex;

public class PlayWarChoosingWhoAnswerState extends PlayState {
    private long interval;

    public PlayWarChoosingWhoAnswerState(PlayContainer container, long interval) {
        super(container, RivalStatus.CHOOSING_WHO_ANSWER);
        this.interval = interval;
    }

    @Override
    public void initCommands() {
        commands.add(new PlaySetDefaultActiveIndexCommand(container));
        commands.add(new PlaySetNextTimeoutCommand(container, interval));
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
