package com.ww.play.state;

import com.ww.model.constant.rival.RivalStatus;
import com.ww.play.container.PlayContainer;

public class PlayAnsweringState extends PlayState {
    public PlayAnsweringState(PlayContainer container) {
        super(container, RivalStatus.ANSWERING);
    }
}
