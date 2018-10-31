package com.ww.game.play.container;

import com.ww.game.GameContainer;
import com.ww.model.constant.rival.RivalImportance;
import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.container.rival.*;
import com.ww.model.container.rival.init.RivalTwoInit;
import com.ww.model.entity.outside.social.Profile;
import com.ww.game.play.state.PlayState;
import lombok.Getter;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

@Getter
public abstract class PlayContainer extends GameContainer {
    protected RivalTwoInit init;
    protected RivalTeams teams;
    protected RivalTasks tasks;
    protected RivalTimeouts timeouts;
    protected RivalDecisions decisions;
    protected RivalResult result;

    public PlayContainer(RivalTwoInit init, RivalTeams teams, RivalTasks tasks, RivalTimeouts timeouts, RivalDecisions decisions, RivalResult result) {
        this.init = init;
        this.teams = teams;
        this.tasks = tasks;
        this.timeouts = timeouts;
        this.decisions = decisions;
        this.result = result;
    }

    public abstract boolean isEnd();

    public abstract boolean isRandomTaskProps();

    public abstract Profile findChoosingTaskPropsProfile();

    public abstract Optional<Profile> findWinner();

    public boolean isStatusEquals(RivalStatus status) {
        return ((PlayState) currentState()).getStatus() == status;
    }

    public boolean isRanking() {
        return init.getImportance() == RivalImportance.RANKING;
    }
}
