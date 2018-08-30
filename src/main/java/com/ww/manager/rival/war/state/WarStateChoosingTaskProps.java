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

public class WarStateChoosingTaskProps extends WarState {

    public WarStateChoosingTaskProps(WarManager manager) {
        super(manager);
    }

    @Override
    protected Flowable<Long> processFlowable() {
        rivalContainer.setStatus(RivalStatus.CHOOSING_TASK_PROPS);
        rivalContainer.increaseCurrentTaskIndex();
        warManager.prepareTask((long) rivalContainer.getCurrentTaskIndex() + 1);
        rivalContainer.setEndChoosingTaskPropsDate(Instant.now().plus(warManager.getRandomChooseTaskPropsInterval(), ChronoUnit.MILLIS));
        for (RivalProfileContainer rivalProfileContainer : warManager.getRivalProfileContainers()) {
            WarProfileContainer warProfileContainer = (WarProfileContainer) rivalProfileContainer;
            warProfileContainer.randomActiveIndex(rivalContainer.getCurrentTaskIndex());
        }
        rivalContainer.forEachProfile(rivalProfileContainer -> {
            Map<String, Object> model = new HashMap<>();
            rivalContainer.fillModelChoosingTaskProps(model, rivalProfileContainer);
            warManager.send(model, warManager.getMessageContent(), rivalProfileContainer.getProfileId());
        });
        return Flowable.intervalRange(0L, 1L, warManager.getRandomChooseTaskPropsInterval(), warManager.getRandomChooseTaskPropsInterval(), TimeUnit.MILLISECONDS);
    }
}
