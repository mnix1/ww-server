package com.ww.play.state;

import com.ww.model.constant.rival.RivalStatus;
import com.ww.play.PlayState;

import java.util.Map;

public class PlayAnsweredState extends PlayState {
    protected Long profileId;
    protected Map<String, Object> content;

    public PlayAnsweredState(Long profileId, Map<String, Object> content) {
        this.status = RivalStatus.ANSWERED;
        this.profileId = profileId;
        this.content = content;
    }
}
