package com.ww.game.play.action;

import com.ww.model.constant.rival.RivalStatus;
import com.ww.game.play.PlayManager;

import java.util.Map;

public class PlaySurrenderAction extends PlayAction {

    public PlaySurrenderAction(PlayManager manager) {
        super(manager);
    }

    @Override
    public void perform(Long profileId, Map<String, Object> content) {
        if (getFlow().isStatusEquals(RivalStatus.CLOSED) || getFlow().isStatusEquals(RivalStatus.DISPOSED)) {
            return;
        }
        getFlow().surrenderAction(profileId);
    }
}
