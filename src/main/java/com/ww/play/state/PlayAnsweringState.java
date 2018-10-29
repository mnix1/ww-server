package com.ww.play.state;

import com.ww.model.constant.rival.RivalStatus;
import com.ww.play.PlayState;

public class PlayAnsweringState extends PlayState {
    public PlayAnsweringState() {
        this.status = RivalStatus.ANSWERING;
    }
}
