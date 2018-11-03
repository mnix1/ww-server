package com.ww.game.play;

import com.ww.game.play.communication.PlayCommunication;
import com.ww.game.play.container.PlayContainer;
import com.ww.game.play.flow.PlayFlow;
import com.ww.model.container.rival.RivalDecisions;
import com.ww.model.container.rival.RivalResult;
import com.ww.model.container.rival.RivalTasks;
import com.ww.model.container.rival.RivalTimeouts;
import com.ww.service.RivalService;
import com.ww.service.social.ProfileConnectionService;
import lombok.Getter;

import java.util.Map;

@Getter
public class PlayManager {
    protected RivalService service;
    protected PlayContainer container;
    protected PlayFlow flow;
    protected PlayCommunication communication;

    protected PlayManager(RivalService service) {
        this.service = service;
    }

    protected RivalTasks prepareTasks() {
        return new RivalTasks(service);
    }

    protected RivalTimeouts prepareTimeouts() {
        return new RivalTimeouts();
    }

    protected RivalDecisions prepareDecisions() {
        return new RivalDecisions();
    }

    protected RivalResult prepareResult() {
        return new RivalResult();
    }

    public ProfileConnectionService getProfileConnectionService() {
        return service.getProfileConnectionService();
    }

    public void sendModelFromBeginning(Long profileId) {
        communication.sendModelFromBeginning(container.getTeams().team(profileId), container.getTeams().opponent(profileId));
    }

    public void updateProfilesElo(){
        service.updateProfilesElo(container);
    }

    public boolean processMessage(Long profileId, Map<String, Object> content) {
        return communication.processMessage(profileId, content);
    }

    public void start() {
        flow.start();
    }

    public void dispose() {
        service.disposeManager(this);
    }
}
