package com.ww.game.auto.state.rival;

import com.ww.game.auto.AutoManager;
import com.ww.game.auto.container.AutoPlayContainer;
import com.ww.game.auto.state.AutoState;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

import static com.ww.game.GameFlow.prepareFlowable;
import static com.ww.service.rival.global.RivalMessageService.CHOOSE_WHO_ANSWER;

public class AutoRivalState extends AutoState {
    protected AutoPlayContainer container;

    @Getter
    protected Map<String, Object> model;

    public AutoRivalState(AutoManager manager) {
        super(manager);
        this.container = manager.getAutoPlayContainer();
    }

    protected AutoRivalState newModel() {
        model = new HashMap<>();
        return this;
    }

    protected Map<String, Object> newModel(String key, Object value) {
        newModel();
        putModel(key, value);
        return getModel();
    }

    protected AutoRivalState putModel(String key, Object value) {
        model.put(key, value);
        return this;
    }

    protected void sendAfter(long interval, String id, Map<String, Object> params) {
        prepareFlowable(interval).subscribe(aLong -> {
            manager.getCommunication().send(id, params);
        });
    }

}
