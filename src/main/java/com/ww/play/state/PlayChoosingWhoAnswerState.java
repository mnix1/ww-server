package com.ww.play.state;

import com.ww.model.constant.rival.RivalStatus;
import com.ww.play.container.PlayContainer;
import com.ww.play.state.PlayState;

public class PlayChoosingWhoAnswerState extends PlayState {
    public PlayChoosingWhoAnswerState(PlayContainer container) {
        super(container,  RivalStatus.CHOOSING_WHO_ANSWER);
    }
}
