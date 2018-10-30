package com.ww.play.action;

import com.ww.model.constant.rival.RivalStatus;
import com.ww.play.PlayManager;

import java.util.Map;

public class PlayChooseTaskPropsAction extends PlayAction {

    public PlayChooseTaskPropsAction(PlayManager manager) {
        super(manager);
    }

    @Override
    public void perform(Long profileId, Map<String, Object> content) {
        if (!container.isStatusEquals(RivalStatus.CHOOSING_TASK_PROPS)) {
            return;
        }
        flow.chosenTaskPropsAction(profileId, content);
    }
}
