package com.ww.game.play.state;

import com.ww.game.GameState;
import com.ww.game.play.PlayManager;
import com.ww.game.play.container.PlayContainer;
import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.container.rival.RivalTeam;
import lombok.Getter;

import java.util.Map;

import static com.ww.game.play.modelfiller.PlayModelFiller.fillModelStatus;

@Getter
public abstract class PlayState extends GameState {
    protected PlayManager manager;

    protected RivalStatus status;

    public PlayState(PlayManager manager, RivalStatus status) {
        this.manager = manager;
        this.status = status;
    }

    protected PlayContainer getContainer() {
        return manager.getContainer();
    }

    public boolean hasAfter() {
        return true;
    }

    public boolean afterReady() {
        return true;
    }

    public long afterInterval() {
        return 0;
    }

    public void after() {
    }

    @Override
    public Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam) {
        Map<String, Object> model = super.prepareModel(team, opponentTeam);
        fillModelStatus(model, status);
        return model;
    }

}
