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

    public AutoRivalState(AutoManager manager) {
        super(manager);
        this.container = manager.getAutoPlayContainer();
    }

    protected void sendAfter(long interval, String id, Map<String, Object> params) {
        prepareFlowable(interval).subscribe(aLong -> {
            manager.getCommunication().send(id, params);
        });
    }

}
