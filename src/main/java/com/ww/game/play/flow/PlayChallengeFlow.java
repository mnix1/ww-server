package com.ww.game.play.flow;

import com.ww.game.play.PlayManager;
import com.ww.game.play.state.PlayAnsweredState;
import com.ww.game.play.state.PlayState;
import com.ww.game.play.state.challenge.PlayChallengeAnsweredState;
import com.ww.game.play.state.challenge.PlayChallengeAnsweringTimeoutState;
import com.ww.game.play.state.challenge.PlayChallengeChangingTaskState;
import com.ww.game.play.state.challenge.PlayChallengeChoosingWhoAnswerState;
import com.ww.game.play.state.war.PlayChangingTaskState;

public class PlayChallengeFlow extends PlayWarFlow {

    public PlayChallengeFlow(PlayManager manager) {
        super(manager);
    }

    @Override
    protected synchronized PlayState createChoosingWhoAnswerState() {
        return new PlayChallengeChoosingWhoAnswerState(manager);
    }

    @Override
    protected synchronized PlayAnsweredState createAnsweredState(Long profileId, Long answerId) {
        return new PlayChallengeAnsweredState(manager, profileId, answerId);
    }

    @Override
    protected synchronized PlayChallengeAnsweringTimeoutState createAnsweringTimeoutState() {
        return new PlayChallengeAnsweringTimeoutState(manager);
    }

    @Override
    protected PlayChangingTaskState createChangingTaskState() {
        return new PlayChallengeChangingTaskState(manager);
    }
}
