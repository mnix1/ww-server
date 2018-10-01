package com.ww.manager.rival.state;

import com.ww.manager.rival.RivalManager;
import com.ww.model.constant.rival.RivalStatus;

public class StateChoosingTaskPropsTimeout extends State {

    public StateChoosingTaskPropsTimeout(RivalManager manager) {
        super(manager);
    }

    @Override
    protected void processVoid() {
        if (manager.getContainer().getStatus() != RivalStatus.CHOOSING_TASK_PROPS) {
            return;
        }
        manager.getContainer().setStatus(RivalStatus.CHOOSING_TASK_PROPS_TIMEOUT);
        manager.prepareTask((long) manager.getContainer().getCurrentTaskIndex() + 1, manager.getContainer().getChosenCategory(), manager.getContainer().getChosenDifficulty());
    }
}
