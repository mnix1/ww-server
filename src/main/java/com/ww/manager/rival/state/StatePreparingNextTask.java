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
        rivalContainer.setNextTaskDate(Instant.now().plus(rivalManager.getPreparingNextTaskInterval(), ChronoUnit.MILLIS));
        rivalContainer.setStatus(RivalStatus.PREPARING_NEXT_TASK);
        rivalContainer.forEachProfile(rivalProfileContainer -> {
            Map<String, Object> model = new HashMap<>();
            rivalManager.getModelFactory().fillModelPreparingNextTask(model, rivalProfileContainer);
            rivalManager.send(model, rivalManager.getMessageContent(), rivalProfileContainer.getProfileId());
        });
        return Flowable.intervalRange(0L, 1L, rivalManager.getPreparingNextTaskInterval(), rivalManager.getPreparingNextTaskInterval(), TimeUnit.MILLISECONDS);
    }
}
