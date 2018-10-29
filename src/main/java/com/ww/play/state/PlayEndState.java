package com.ww.play.state;

import com.ww.model.constant.rival.RivalStatus;
import com.ww.play.PlayState;

public class PlayEndState extends PlayState {
    public PlayEndState() {
        this.status = RivalStatus.CLOSED;
    }
}
