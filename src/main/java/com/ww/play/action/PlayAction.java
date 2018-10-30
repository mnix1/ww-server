package com.ww.play.action;

import com.ww.play.flow.PlayFlow;
import com.ww.play.container.PlayContainer;
import com.ww.play.PlayManager;

import java.util.Map;

public class PlayAction {

    protected PlayContainer container;
    protected PlayFlow flow;

    public PlayAction(PlayManager manager) {
        this.container = manager.getContainer();
        this.flow = manager.getFlow();
    }

    public void perform(Long profileId, Map<String, Object> content) {
    }
}
