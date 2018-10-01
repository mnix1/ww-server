package com.ww.manager.rival.state;

import com.ww.manager.rival.RivalManager;
import com.ww.model.constant.rival.RivalStatus;
import io.reactivex.Flowable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class StateIntro extends State {

    public StateIntro(RivalManager manager) {
        super(manager);
    }

    @Override
    protected Flowable<Long> processFlowable() {
        rivalContainer.setStatus(RivalStatus.INTRO);
        rivalContainer.forEachProfile(profileContainer -> {
            Map<String, Object> model = new HashMap<>();
            rivalManager.getModelFactory().fillModelIntro(model, profileContainer);
            rivalManager.send(model, rivalManager.getMessageContent(), profileContainer.getProfileId());
        });
        return Flowable.intervalRange(0L, 1L, rivalManager.getIntroInterval(), rivalManager.getIntroInterval(), TimeUnit.MILLISECONDS);
    }
}
