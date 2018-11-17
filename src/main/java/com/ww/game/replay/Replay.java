package com.ww.game.replay;

import com.ww.game.GameState;
import com.ww.game.play.PlayManager;
import io.reactivex.Flowable;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

public class Replay {
    private PlayManager manager;
    private List<GameState> states;

    public Replay(PlayManager manager) {
        this.manager = manager;
        this.states = manager.getFlow().getStates();
    }

    public void play() {
        GameState firstState = states.get(0);
        run(firstState, findNextState(firstState).orElse(null));
    }

    public void run(GameState state, GameState nextState) {
        state.execute();
        state.updateNotify();
        if (nextState == null) {
            return;
        }
        long interval = nextState.getDate().toEpochMilli() - state.getDate().toEpochMilli();
        if (interval <= 0) {
            run(nextState, findNextState(nextState).orElse(null));
        } else {
            Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS).subscribe(aLong -> {
                run(nextState, findNextState(nextState).orElse(null));
            });
        }
    }

    private Optional<GameState> findNextState(GameState state) {
        for (int i = 0; i < states.size(); i++) {
            if (states.get(i) == state && i + 1 < states.size()) {
                return Optional.of(states.get(i + 1));
            }
        }
        return Optional.empty();
    }
}
