package com.ww.play.state;

import com.ww.model.constant.rival.RivalStatus;
import com.ww.play.PlayState;

public class PlayRandomTaskPropsState extends PlayState {
    public PlayRandomTaskPropsState() {
        this.status = RivalStatus.CHO;
    }
}
