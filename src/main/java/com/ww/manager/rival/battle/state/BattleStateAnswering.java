package com.ww.manager.rival.battle.state;

import com.ww.manager.rival.RivalManager;
import com.ww.manager.rival.state.State;
import com.ww.model.constant.rival.RivalStatus;
import io.reactivex.Flowable;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class BattleStateAnswering extends State {

    public BattleStateAnswering(RivalManager manager) {
        super(manager);
    }

    @Override
    protected Flowable<Long> processFlowable() {
        rivalContainer.setEndAnsweringDate(Instant.now().plus(rivalManager.getAnsweringInterval(), ChronoUnit.MILLIS));
        rivalContainer.setStatus(RivalStatus.ANSWERING);
        rivalContainer.forEachProfile(rivalProfileContainer -> {
            Map<String, Object> model = new HashMap<>();
            rivalManager.getModelFactory().fillModelAnswering(model, rivalProfileContainer);
            rivalManager.send(model, rivalManager.getMessageContent(), rivalProfileContainer.getProfileId());
        });
        return Flowable.intervalRange(0L, 1L, rivalManager.getAnsweringInterval(), rivalManager.getAnsweringInterval(), TimeUnit.MILLISECONDS);
    }
}
