package com.ww.manager.rival.war.state;

import com.ww.manager.rival.war.WarManager;
import com.ww.model.constant.rival.RivalStatus;
import io.reactivex.Flowable;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class WarStateAnswering extends WarState {

    public WarStateAnswering(WarManager manager) {
        super(manager);
    }

    @Override
    protected Flowable<Long> processFlowable() {
        manager.getModel().setEndAnsweringDate(Instant.now().plus(manager.getInterval().getAnsweringInterval(), ChronoUnit.MILLIS));
        manager.getModel().setStatus(RivalStatus.ANSWERING);

        manager.getModel().updateWisieAnswerManagers(manager);

        manager.getModel().getTeamsContainer().forEachTeam(profileContainer -> {
            Map<String, Object> model = new HashMap<>();
            manager.getModelFactory().fillModelAnswering(model, profileContainer);
            manager.send(model, manager.getMessageContent(), profileContainer.getProfileId());
        });
        manager.getModel().startWisieAnswerManager();
        return Flowable.intervalRange(0L, 1L, manager.getInterval().getAnsweringInterval(), manager.getInterval().getAnsweringInterval(), TimeUnit.MILLISECONDS);
    }
}
