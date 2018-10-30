package com.ww.play.state;

import com.ww.model.constant.rival.RivalStatus;
import com.ww.play.container.PlayContainer;

public class PlayChoosingTaskPropsState extends PlayState {
    public PlayChoosingTaskPropsState(PlayContainer container) {
        super(container, RivalStatus.CHOOSING_TASK_PROPS);
    }
}
