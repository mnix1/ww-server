package com.ww.play.flow;

import com.ww.model.container.rival.RivalInterval;
import com.ww.play.PlayManager;
import com.ww.play.state.PlayChoosingTaskCategoryState;
import com.ww.play.state.PlayEndState;
import com.ww.play.state.PlayIntroState;
import com.ww.play.state.PlayState;
import com.ww.play.state.battle.PlayBattleAnsweredState;
import com.ww.play.state.battle.PlayBattleChoosingTaskCategoryState;
import com.ww.play.state.battle.PlayBattleEndState;
import com.ww.play.state.battle.PlayBattleIntroState;

public class PlayBattleFlow extends PlayFlow {

    public PlayBattleFlow(PlayManager manager, RivalInterval interval) {
        super(manager, interval);
    }

    @Override
    protected synchronized PlayIntroState createIntroState() {
        return new PlayBattleIntroState(manager.getContainer());
    }

    @Override
    protected synchronized PlayState createAnsweredState(Long profileId, Long answerId) {
        return new PlayBattleAnsweredState(getContainer(), profileId, answerId);
    }

    @Override
    protected synchronized PlayChoosingTaskCategoryState createChoosingTaskCategoryState() {
        return new PlayBattleChoosingTaskCategoryState(getContainer(), interval.getChoosingTaskCategoryInterval());
    }

    @Override
    protected synchronized PlayEndState createEndState() {
        return new PlayBattleEndState(getContainer(), manager);
    }
}
