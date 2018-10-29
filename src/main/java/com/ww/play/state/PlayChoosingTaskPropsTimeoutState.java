package com.ww.play.state;

import com.ww.model.constant.rival.RivalStatus;
import com.ww.play.PlayState;

public class PlayChoosingTaskPropsTimeoutState extends PlayState {
    public PlayChoosingTaskPropsTimeoutState() {
        this.status = RivalStatus.CHOOSING_TASK_PROPS;
    }
}
