package com.ww.play.action;

import com.ww.model.constant.rival.RivalStatus;
import com.ww.play.PlayManager;

import java.util.Map;

public class PlaySurrenderAction extends PlayAction {

    public PlaySurrenderAction(PlayManager manager) {
        super(manager);
    }

    @Override
    public void perform(Long profileId, Map<String, Object> content) {
        if (container.isStatusEquals(RivalStatus.CLOSED) || container.isStatusEquals(RivalStatus.DISPOSED)) {
            return;
        }
        flow.surrenderAction(profileId);
    }
}
