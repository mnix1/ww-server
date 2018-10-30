package com.ww.play;

import com.ww.model.container.rival.RivalTasks;
import com.ww.model.container.rival.RivalTimeouts;
import com.ww.play.communication.PlayCommunication;
import com.ww.play.container.PlayContainer;
import com.ww.play.flow.PlayFlow;
import com.ww.service.RivalService;
import com.ww.service.social.ProfileConnectionService;
import lombok.Getter;

import java.util.Map;

@Getter
public class PlayManager {
    protected RivalService rivalService;
    protected PlayContainer container;
    protected PlayFlow flow;
    protected PlayCommunication communication;

    protected PlayManager(RivalService rivalService) {
        this.rivalService = rivalService;
    }

    protected RivalTasks prepareTasks() {
        return new RivalTasks(rivalService);
    }

    protected RivalTimeouts prepareTimeouts() {
        return new RivalTimeouts();
    }

    public ProfileConnectionService getProfileConnectionService() {
        return rivalService.getProfileConnectionService();
    }

    public void sendModelFromBeginning(Long profileId) {
        communication.sendModelFromBeginning(profileId);
    }

    public boolean processMessage(Long profileId, Map<String, Object> content) {
        return communication.processMessage(profileId, content);
    }

    public void start() {
        flow.introPhase();
    }

    public void dispose() {
    }
}
