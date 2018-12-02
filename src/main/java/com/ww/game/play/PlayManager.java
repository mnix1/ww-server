package com.ww.game.play;

import com.ww.game.play.communication.PlayCommunication;
import com.ww.game.play.container.PlayContainer;
import com.ww.game.play.flow.PlayFlow;
import com.ww.helper.JSONHelper;
import com.ww.model.container.rival.*;
import com.ww.model.entity.outside.rival.Rival;
import com.ww.service.RivalService;
import com.ww.websocket.message.Message;
import com.ww.websocket.message.MessageDTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.scheduling.annotation.Async;

import java.util.Map;

import static com.ww.websocket.message.MessageDTO.rivalContentMessage;

@Getter
public abstract class PlayManager {
    protected RivalService service;
    protected RivalInterval interval;
    protected PlayContainer container;
    protected PlayFlow flow;
    protected PlayCommunication communication;
    @Setter
    protected Rival rival;

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

    public void sendModelFromBeginning(Long profileId) {
        communication.sendModelFromBeginning(profileId);
    }

    @Async
    public void processMessage(Long profileId, Map<String, Object> content) {
        synchronized (flow) {
            communication.processMessage(profileId, content);
        }
    }

    public void send(Long profileId, Map<String, Object> model) {
        service.getConnectionService().sendMessage(profileId, rivalContentMessage(model));
    }

    public void dispose() {
        service.disposeManager(this);
    }

    @Override
    public String toString() {
        return super.toString() + "{" +
                "container=" + container +
                ", flow=" + flow +
                ", rival=" + rival +
                '}';
    }

}
