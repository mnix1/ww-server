package com.ww.play.state;

import com.ww.model.constant.rival.RivalStatus;
import com.ww.play.PlayState;

public class PlayAnsweringTimeoutState extends PlayState {
    public PlayAnsweringTimeoutState() {
        this.status = RivalStatus.ANSWERING_TIMEOUT;
    }
}
