package com.ww.manager.rival.war.state;

import com.ww.manager.rival.war.WarManager;
import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.container.rival.RivalProfileContainer;
import com.ww.model.container.rival.war.WarProfileContainer;
import io.reactivex.Flowable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class WarStateAnsweringTimeout extends WarState {

    public WarStateAnsweringTimeout(WarManager manager) {
        super(manager);
    }

    @Override
    protected Flowable<Long> processFlowable() {
        if (rivalContainer.getStatus() != RivalStatus.ANSWERING) {
            return Flowable.empty();
        }
        warContainer.stopHeroAnswerManager();
        for (RivalProfileContainer rivalProfileContainer : warManager.getRivalProfileContainers()) {
            WarProfileContainer warProfileContainer = (WarProfileContainer) rivalProfileContainer;
            warProfileContainer.removeActiveIndexFromPresentIndexes();
        }
        rivalContainer.setStatus(RivalStatus.ANSWERING_TIMEOUT);
        rivalContainer.forEachProfile(rivalProfileContainer -> {
            Map<String, Object> model = new HashMap<>();
            rivalContainer.fillModelAnsweringTimeout(model, rivalProfileContainer);
            warManager.send(model, warManager.getMessageContent(), rivalProfileContainer.getProfileId());
        });
        return Flowable.intervalRange(0L, 1L, warManager.getAnsweringTimeoutInterval(), warManager.getAnsweringTimeoutInterval(), TimeUnit.MILLISECONDS);
    }
}
