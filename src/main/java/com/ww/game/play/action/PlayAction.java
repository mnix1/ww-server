package com.ww.game.play.action;

import com.ww.game.play.flow.PlayFlow;
import com.ww.game.play.container.PlayContainer;
import com.ww.game.play.PlayManager;

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
