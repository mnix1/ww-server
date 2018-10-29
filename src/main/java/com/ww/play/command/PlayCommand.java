package com.ww.play.command;

import com.ww.play.PlayFlow;
import com.ww.play.PlayModel;

import java.util.Map;

public class PlayCommand {

    protected PlayModel model;
    protected PlayFlow flow;

    public PlayCommand(PlayModel model, PlayFlow flow) {
        this.model = model;
        this.flow = flow;
    }

    public void execute(Long profileId, Map<String, Object> content) {
    }

    public void unexecute(Long profileId, Map<String, Object> content) {
    }
}
