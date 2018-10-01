package com.ww.manager.rival.state;

import com.ww.manager.rival.RivalManager;
import com.ww.model.constant.rival.RivalStatus;
import io.reactivex.Flowable;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class StatePreparingNextTask extends State {

    public StatePreparingNextTask(RivalManager manager) {
        super(manager);
    }

    @Override
    protected Flowable<Long> processFlowable() {
        manager.getContainer().setNextTaskDate(Instant.now().plus(manager.getInterval().getPreparingNextTaskInterval(), ChronoUnit.MILLIS));
        manager.getContainer().setStatus(RivalStatus.PREPARING_NEXT_TASK);
        manager.getContainer().forEachProfile(rivalProfileContainer -> {
            Map<String, Object> model = new HashMap<>();
            manager.getModelFactory().fillModelPreparingNextTask(model, rivalProfileContainer);
            manager.send(model, manager.getMessageContent(), rivalProfileContainer.getProfileId());
        });
        return Flowable.intervalRange(0L, 1L, manager.getInterval().getPreparingNextTaskInterval(), manager.getInterval().getPreparingNextTaskInterval(), TimeUnit.MILLISECONDS);
    }
}
