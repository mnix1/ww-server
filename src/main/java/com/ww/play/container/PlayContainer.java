package com.ww.play.container;

import com.ww.model.constant.rival.RivalImportance;
import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.container.rival.RivalTeams;
import com.ww.model.container.rival.init.RivalTwoPlayerInit;
import com.ww.play.state.PlayState;
import lombok.Getter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
public abstract class PlayContainer {
    protected RivalTwoPlayerInit init;
    protected RivalTeams teams;
    protected List<PlayState> states = new CopyOnWriteArrayList<>();

    public PlayContainer(RivalTwoPlayerInit init, RivalTeams teams) {
        this.init = init;
        this.teams = teams;
    }

    public abstract boolean isEnd();

    public abstract boolean isRandomTaskProps();

    public PlayState currentState() {
        return states.get(states.size() - 1);
    }

    public void addState(PlayState state) {
        states.add(state);
    }

    public boolean isStatusEquals(RivalStatus status) {
        return currentState().getStatus() == status;
    }

    public boolean isRanking() {
        return init.getImportance() == RivalImportance.RANKING;
    }
}
