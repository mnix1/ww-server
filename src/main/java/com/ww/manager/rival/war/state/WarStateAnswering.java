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
        rivalContainer.setEndAnsweringDate(Instant.now().plus(warManager.getAnsweringInterval(), ChronoUnit.MILLIS));
        rivalContainer.setStatus(RivalStatus.ANSWERING);

        warContainer.updateHeroAnswerManagers(warManager);

        rivalContainer.forEachProfile(rivalProfileContainer -> {
            Map<String, Object> model = new HashMap<>();
            rivalContainer.fillModelAnswering(model, rivalProfileContainer);
            warManager.send(model, warManager.getMessageContent(), rivalProfileContainer.getProfileId());
        });
        warContainer.startHeroAnswerManager();
        return Flowable.intervalRange(0L, 1L, warManager.getAnsweringInterval(), warManager.getAnsweringInterval(), TimeUnit.MILLISECONDS);
    }
}
