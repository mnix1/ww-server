package com.ww.manager.rival.state;

import com.ww.manager.rival.RivalManager;
import com.ww.model.constant.rival.RivalStatus;

public class StateChoosingTaskPropsTimeout extends State {

    public StateChoosingTaskPropsTimeout(RivalManager manager) {
        super(manager);
    }

    @Override
    protected void processVoid() {
        if (manager.getModel().getStatus() != RivalStatus.CHOOSING_TASK_PROPS) {
            return;
        }
        manager.getModel().setStatus(RivalStatus.CHOOSING_TASK_PROPS_TIMEOUT);
        manager.prepareTask((long) manager.getModel().getCurrentTaskIndex() + 1, manager.getModel().getChosenCategory(), manager.getModel().getChosenDifficulty());
    }
}
