package com.ww.manager.rival.battle.state;

import com.ww.manager.rival.RivalManager;
import com.ww.manager.rival.state.State;
import com.ww.model.constant.rival.RivalStatus;
import io.reactivex.Flowable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class BattleStateAnsweringTimeout extends State {

    public BattleStateAnsweringTimeout(RivalManager manager) {
        super(manager);
    }

    @Override
    protected Flowable<Long> processFlowable() {
        if (rivalContainer.getStatus() != RivalStatus.ANSWERING) {
            return Flowable.empty();
        }
        rivalContainer.setStatus(RivalStatus.ANSWERING_TIMEOUT);
        rivalContainer.forEachProfile(rivalProfileContainer -> {
            Map<String, Object> model = new HashMap<>();
            rivalContainer.fillModelAnsweringTimeout(model, rivalProfileContainer);
            rivalManager.send(model, rivalManager.getMessageContent(), rivalProfileContainer.getProfileId());
        });
        return Flowable.intervalRange(0L, 1L, rivalManager.getAnsweringTimeoutInterval(), rivalManager.getAnsweringTimeoutInterval(), TimeUnit.MILLISECONDS);
    }
}
