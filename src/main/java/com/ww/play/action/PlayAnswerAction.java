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
        if (!container.isStatusEquals(RivalStatus.ANSWERING)
                || !content.containsKey("answerId")) {
            return;
        }
        Long answerId = null;
        try {
            answerId = ((Integer) content.get("answerId")).longValue();
        } catch (Exception e) {
        }
        flow.answeredAction(profileId, answerId);
    }
}
