package com.ww.play.state;

import com.ww.model.constant.rival.RivalStatus;
import com.ww.play.container.PlayContainer;

public class PlayPreparingNextTaskState extends PlayState {
    public PlayPreparingNextTaskState(PlayContainer container) {
        super(container, RivalStatus.PREPARING_NEXT_TASK);
    }
}
