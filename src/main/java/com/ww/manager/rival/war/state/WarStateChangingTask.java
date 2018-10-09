package com.ww.manager.rival.war.state;

import com.ww.manager.rival.war.WarManager;
import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.war.WarTeam;
import io.reactivex.Flowable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class WarStateChangingTask extends WarState {

    public WarStateChangingTask(WarManager manager) {
        super(manager, STATE_TYPE_FLOWABLE);
    }

    @Override
    protected Flowable<Long> processFlowable() {
        if (manager.getModel().getStatus() != RivalStatus.ANSWERING) {
            return Flowable.empty();
        }
        manager.getModel().stopWisieAnswerManager();
        manager.getModel().setStatus(RivalStatus.CHANGING_TASK);
        Map<String, Object> model = new HashMap<>();
        manager.getModelFactory().fillModelStatus(model);
        manager.getModel().getTeams().forEachTeam(team -> {
            manager.send(model, manager.getMessageContent(), team.getProfileId());
        });
        return Flowable.intervalRange(0L, 1L, manager.getInterval().getChangingTaskInterval(), manager.getInterval().getChangingTaskInterval(), TimeUnit.MILLISECONDS);
    }
}
