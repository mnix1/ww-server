package com.ww.game.play.state;

import com.ww.game.GameState;
import com.ww.game.play.PlayManager;
import com.ww.game.play.container.PlayContainer;
import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.RivalTeams;
import lombok.Getter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.ww.game.play.modelfiller.PlayModelFiller.fillModelStatus;

@Getter
public abstract class PlayState extends GameState {
    protected PlayManager manager;
    protected RivalStatus status;
    protected List<GameState> childStates = new CopyOnWriteArrayList<>();

    public PlayState(PlayManager manager, RivalStatus status) {
        this.manager = manager;
        this.status = status;
    }

    protected PlayContainer getContainer() {
        return manager.getContainer();
    }

    public Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam) {
        Map<String, Object> model = new HashMap<>();
        fillModelStatus(model, status);
        return model;
    }

    @Override
    public void updateNotify() {
        RivalTeams teams = getContainer().getTeams();
        teams.forEachTeam(team -> {
            manager.getCommunication().sendAndUpdateModel(team.getProfileId(), this.prepareModel(team, teams.opponent(team)));
        });
    }

    public Map<String, Object> prepareChildModel(RivalTeam team, RivalTeam opponentTeam) {
        return new HashMap<>();
    }

    public void addChildState(GameState state) {
        childStates.add(state);
    }

    @Override
    public String toString() {
        return super.toString() + ", " + manager.getContainer().toString();
    }
}
