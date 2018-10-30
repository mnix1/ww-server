package com.ww.play.container;

import com.ww.model.constant.rival.RivalImportance;
import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.container.rival.RivalTimeouts;
import com.ww.model.container.rival.RivalTasks;
import com.ww.model.container.rival.RivalTeams;
import com.ww.model.container.rival.init.RivalTwoInit;
import com.ww.play.state.PlayState;
import lombok.Getter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
public abstract class PlayContainer {
    protected RivalTwoInit init;
    protected List<PlayState> states = new CopyOnWriteArrayList<>();
    protected RivalTeams teams;
    protected RivalTasks tasks;
    protected RivalTimeouts timeouts;

    public PlayContainer(RivalTwoInit init, RivalTeams teams, RivalTasks tasks, RivalTimeouts timeouts) {
        this.init = init;
        this.teams = teams;
        this.tasks = tasks;
        this.timeouts = timeouts;
    }

    public abstract boolean isEnd();

    public abstract boolean isRandomTaskProps();

    public abstract String findChoosingTaskPropsTag();

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
