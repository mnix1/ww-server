package com.ww.play;

import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.container.rival.RivalTeams;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class PlayModel {
    protected RivalTeams teams;
    protected List<PlayState> states = new CopyOnWriteArrayList<>();

    public PlayModel(RivalTeams teams) {
        this.teams = teams;
    }

    public PlayState current() {
        return states.get(states.size() - 1);
    }

    public void add(PlayState state) {
        states.add(state);
    }

    public boolean isStatusEquals(RivalStatus status) {
        return current().status == status;
    }

    public boolean isEnd() {
        return false;
    }

    public boolean isRandomTaskProps() {
        return true;
    }
}
