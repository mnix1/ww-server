package com.ww.game.play.state;

import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.container.rival.RivalTeam;
import com.ww.game.play.PlayManager;
import com.ww.game.play.command.PlaySetResultCommand;
import com.ww.game.play.command.PlayUpdateEloCommand;
import com.ww.game.play.container.PlayContainer;
import com.ww.service.RivalService;

import java.util.Map;

import static com.ww.game.play.modelfiller.PlayModelFiller.fillModelCorrectAnswerId;
import static com.ww.game.play.modelfiller.PlayModelFiller.fillModelEnd;
import static com.ww.game.play.modelfiller.PlayModelFiller.fillModelSeasons;

public class PlayEndState extends PlayState {
    private PlayManager manager;

    public PlayEndState(PlayManager manager) {
        super(manager.getContainer(), RivalStatus.CLOSED);
        this.manager = manager;
    }

    @Override
    public void initCommands() {
        commands.add(new PlaySetResultCommand(container));
        commands.add(new PlayUpdateEloCommand(manager));
    }

    @Override
    public Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam) {
        Map<String, Object> model = super.prepareModel(team, opponentTeam);
        fillModelEnd(model, container);
        fillModelSeasons(model, container, team, opponentTeam);
        return model;
    }
}
