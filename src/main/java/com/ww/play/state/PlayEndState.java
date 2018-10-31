package com.ww.play.state;

import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.container.rival.RivalTeam;
import com.ww.play.command.PlaySetResultCommand;
import com.ww.play.container.PlayContainer;

import java.util.Map;

import static com.ww.play.modelfiller.PlayModelFiller.fillModelCorrectAnswerId;
import static com.ww.play.modelfiller.PlayModelFiller.fillModelEnd;

public class PlayEndState extends PlayState {
    public PlayEndState(PlayContainer container) {
        super(container, RivalStatus.CLOSED);
    }

    @Override
    public void initCommands() {
        commands.add(new PlaySetResultCommand(container));
    }

    @Override
    public Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam) {
        Map<String, Object> model = super.prepareModel(team, opponentTeam);
        fillModelEnd(model, container);
        return model;
    }
}
