package com.ww.play;

import com.ww.model.container.rival.init.RivalInit;
import com.ww.play.command.PlayCommand;
import com.ww.service.RivalService;
import com.ww.websocket.message.Message;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public abstract class PlayManager {
    protected RivalService rivalService;
    protected RivalInit initContainer;
    protected PlayModel model;
    protected PlayFlow flow;
    protected Map<String, PlayCommand> commandMap = new ConcurrentHashMap<>();
    protected PlayCommunication communication;

    protected PlayManager(RivalInit initContainer, RivalService rivalService) {
        this.initContainer = initContainer;
        this.rivalService = rivalService;
    }

    public abstract Message getMessageContent();

    public void start() {
        flow.introPhase();
    }

    public void dispose() {
    }

    public synchronized boolean processMessage(Long profileId, Map<String, Object> content) {
        String id = (String) content.get("id");
        if (commandMap.containsKey(id)) {
            commandMap.get(id).execute(profileId, content);
            return true;
        }
        return false;
    }

}
