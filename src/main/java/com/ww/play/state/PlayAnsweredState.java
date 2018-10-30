package com.ww.play.state;

import com.ww.model.constant.rival.RivalStatus;
import com.ww.play.container.PlayContainer;

import java.util.Map;

public class PlayAnsweredState extends PlayState {
    protected Long profileId;
    protected Map<String, Object> content;

    public PlayAnsweredState(PlayContainer container, Long profileId, Map<String, Object> content) {
        super(container, RivalStatus.ANSWERED);
        this.profileId = profileId;
        this.content = content;
    }
}
