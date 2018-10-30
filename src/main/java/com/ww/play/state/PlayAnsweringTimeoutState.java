package com.ww.play.state;

import com.ww.model.constant.rival.RivalStatus;
import com.ww.play.container.PlayContainer;

public class PlayAnsweringTimeoutState extends PlayState {
    public PlayAnsweringTimeoutState(PlayContainer container) {
        super(container, RivalStatus.ANSWERING_TIMEOUT);
    }
}
