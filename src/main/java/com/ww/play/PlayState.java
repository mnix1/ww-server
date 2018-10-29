package com.ww.play;

import com.ww.model.constant.rival.RivalStatus;
import lombok.Getter;

import java.time.Instant;
import java.util.Map;

@Getter
public abstract class PlayState {
    protected RivalStatus status;
    protected Instant date = Instant.now();

    public void process() {
    }

    public void fillModel(Map<String, Object> model) {
        model.put("status", status);
    }
}
