package com.ww.play.action;

import com.ww.model.constant.rival.RivalStatus;
import com.ww.play.PlayManager;
import com.ww.play.flow.PlayWarFlow;

import java.util.Map;

public class PlayChooseWhoAnswerAction extends PlayAction {

    public PlayChooseWhoAnswerAction(PlayManager manager) {
        super(manager);
    }

    @Override
    public void perform(Long profileId, Map<String, Object> content) {
        if (!container.isStatusEquals(RivalStatus.CHOOSING_WHO_ANSWER)) {
            return;
        }
        ((PlayWarFlow) flow).choosingWhoAnswerPhase();
    }
}
