package com.ww.play.state;

import com.ww.model.constant.rival.RivalStatus;
import com.ww.play.PlayState;

import java.util.Map;

public class PlayChosenTaskPropsState extends PlayState {
    public PlayChosenTaskPropsState(Long profileId, Map<String, Object> content) {
        this.status = RivalStatus.CHOOSING_TASK_PROPS;
    }

    public boolean isDone() {
        return false;
    }
}
