package com.ww.manager.rival.state;

import com.ww.manager.rival.RivalManager;
import com.ww.model.constant.rival.RivalStatus;
import io.reactivex.Flowable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class StateChoosingTaskPropsTimeout extends State {

    public StateChoosingTaskPropsTimeout(RivalManager manager) {
        super(manager);
    }

    @Override
    protected void processVoid() {
        if (rivalContainer.getStatus() != RivalStatus.CHOOSING_TASK_PROPS) {
            return;
        }
        rivalContainer.setStatus(RivalStatus.CHOOSING_TASK_PROPS_TIMEOUT);
        rivalManager.prepareTask((long) rivalContainer.getCurrentTaskIndex() + 1, rivalContainer.getChosenCategory(), rivalContainer.getChosenDifficulty());
    }
}
