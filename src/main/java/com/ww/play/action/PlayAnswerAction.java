package com.ww.play.action;

import com.ww.model.constant.rival.RivalStatus;
import com.ww.play.PlayManager;

import java.util.Map;

public class PlayAnswerAction extends PlayAction {


    public PlayAnswerAction(PlayManager manager) {
        super(manager);
    }

    @Override
    public void perform(Long profileId, Map<String, Object> content) {
        if (!container.isStatusEquals(RivalStatus.ANSWERING)) {
            return;
        }
        flow.answeredAction(profileId, content);
    }
}
