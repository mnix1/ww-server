package com.ww.game.play.state;

import com.ww.game.GameState;
import com.ww.game.play.PlayManager;
import com.ww.game.play.container.PlayContainer;
import com.ww.game.play.modelfiller.PlayModelPreparer;
import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.container.rival.RivalTeam;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.ww.game.play.modelfiller.PlayModelFiller.fillModelStatus;

@Getter
public abstract class PlayState extends GameState implements PlayModelPreparer {
    protected PlayManager manager;
    protected RivalStatus status;

    public PlayState(PlayManager manager, RivalStatus status) {
        this.manager = manager;
        this.status = status;
    }

    protected PlayContainer getContainer() {
        return manager.getContainer();
    }

    @Override
    public Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam) {
        Map<String, Object> model = new HashMap<>();
        fillModelStatus(model, status);
        return model;
    }

    @Override
    public void updateNotify() {
        manager.getCommunication().prepareModel(this);
        manager.getCommunication().sendPreparedModel();
    }

    public Map<String, Object> prepareChildModel(RivalTeam team, RivalTeam opponentTeam) {
        return new HashMap<>();
    }
}
