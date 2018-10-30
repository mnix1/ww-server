package com.ww.play.command;

import com.ww.play.flow.PlayFlow;
import com.ww.play.container.PlayContainer;
import com.ww.play.PlayManager;

import java.util.Map;

public class PlayCommand {

    protected PlayContainer container;
    protected PlayFlow flow;

    public PlayCommand(PlayManager manager) {
        this.container = manager.getContainer();
        this.flow = manager.getFlow();
    }

    public void execute(Long profileId, Map<String, Object> content) {
    }

    public void unexecute(Long profileId, Map<String, Object> content) {
    }
}
