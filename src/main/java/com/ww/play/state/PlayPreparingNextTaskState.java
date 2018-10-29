package com.ww.play.state;

import com.ww.model.constant.rival.RivalStatus;
import com.ww.play.PlayState;

public class PlayPreparingNextTaskState extends PlayState {
    public PlayPreparingNextTaskState() {
        this.status = RivalStatus.PREPARING_NEXT_TASK;
    }
}
