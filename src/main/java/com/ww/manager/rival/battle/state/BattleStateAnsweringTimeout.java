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
        if (manager.getModel().getStatus() != RivalStatus.ANSWERING) {
            return Flowable.empty();
        }
        manager.getModel().setStatus(RivalStatus.ANSWERING_TIMEOUT);
        manager.getModel().getTeamsContainer().forEachProfile(profileContainer -> {
            Map<String, Object> model = new HashMap<>();
            manager.getModelFactory().fillModelAnsweringTimeout(model, profileContainer);
            manager.send(model, manager.getMessageContent(), profileContainer.getProfileId());
        });
        return Flowable.intervalRange(0L, 1L, manager.getInterval().getAnsweringTimeoutInterval(), manager.getInterval().getAnsweringTimeoutInterval(), TimeUnit.MILLISECONDS);
    }
}
