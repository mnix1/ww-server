package com.ww.game.play.state;

import com.ww.game.GameState;
import com.ww.game.play.container.PlayContainer;
import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.container.rival.RivalTeam;
import lombok.Getter;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static com.ww.game.play.modelfiller.PlayModelFiller.fillModelStatus;

@Getter
public abstract class PlayState extends GameState {
    protected PlayContainer container;

    protected RivalStatus status;

    public PlayState(PlayContainer container, RivalStatus status) {
        this.container = container;
        this.status = status;
    }

    @Override
    public Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam) {
        Map<String, Object> model = super.prepareModel(team, opponentTeam);
        fillModelStatus(model, status);
        return model;
    }

}
