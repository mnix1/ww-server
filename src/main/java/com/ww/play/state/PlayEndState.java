package com.ww.play.state;

import com.ww.model.constant.rival.RivalStatus;
import com.ww.play.container.PlayContainer;

public class PlayEndState extends PlayState {
    public PlayEndState(PlayContainer container) {
        super(container, RivalStatus.CLOSED);
    }
}
