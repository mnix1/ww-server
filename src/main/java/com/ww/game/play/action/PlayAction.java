package com.ww.game.play.action;

import com.ww.game.play.PlayManager;
import com.ww.game.play.container.PlayContainer;
import com.ww.game.play.flow.PlayFlow;

import java.util.Map;

public class PlayAction {

    protected PlayManager manager;

    public PlayAction(PlayManager manager) {
        this.manager = manager;
    }

    protected PlayContainer getContainer() {
        return manager.getContainer();
    }

    protected PlayFlow getFlow() {
        return manager.getFlow();
    }

    public void perform(Long profileId, Map<String, Object> content) {
    }
}
