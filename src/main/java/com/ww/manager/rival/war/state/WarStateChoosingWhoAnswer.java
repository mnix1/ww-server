package com.ww.manager.rival.war.state;

import com.ww.manager.rival.war.WarManager;
import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.container.rival.RivalProfileContainer;
import com.ww.model.container.rival.war.WarProfileContainer;
import io.reactivex.Flowable;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class WarStateChoosingWhoAnswer extends WarState {

    public WarStateChoosingWhoAnswer(WarManager manager) {
        super(manager);
    }

    @Override
    protected Flowable<Long> processFlowable() {
        rivalContainer.setStatus(RivalStatus.CHOOSING_WHO_ANSWER);

        for (RivalProfileContainer rivalProfileContainer : warManager.getRivalProfileContainers()) {
            WarProfileContainer warProfileContainer = (WarProfileContainer) rivalProfileContainer;
            warProfileContainer.setActiveIndex(warProfileContainer.getPresentIndexes().get(0));
            warProfileContainer.setChosenActiveIndex(false);
        }

        int interval = warManager.getChoosingWhoAnswerInterval();
        warContainer.setEndChoosingWhoAnswerDate(Instant.now().plus(interval, ChronoUnit.MILLIS));
        warContainer.forEachProfile(rivalProfileContainer -> {
            Map<String, Object> model = new HashMap<>();
            warContainer.fillModelChoosingWhoAnswer(model, rivalProfileContainer);
            rivalManager.send(model, rivalManager.getMessageContent(), rivalProfileContainer.getProfileId());
        });
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
