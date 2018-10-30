package com.ww.play.state;

import com.ww.model.constant.rival.RivalStatus;
import com.ww.play.container.PlayContainer;

public class PlayChoosingTaskPropsTimeoutState extends PlayState {
    public PlayChoosingTaskPropsTimeoutState(PlayContainer container) {
        super(container, RivalStatus.CHOOSING_TASK_PROPS);
    }
}
