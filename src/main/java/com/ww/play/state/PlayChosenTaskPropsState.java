package com.ww.play.state;

import com.ww.model.constant.rival.RivalStatus;
import com.ww.play.container.PlayContainer;

import java.util.Map;

public class PlayChosenTaskPropsState extends PlayState {
    public PlayChosenTaskPropsState(PlayContainer container, Long profileId, Map<String, Object> content) {
        super(container, RivalStatus.CHOOSING_TASK_PROPS);
    }

    public boolean isDone() {
        return false;
    }
}
