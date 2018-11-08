package com.ww.game.play.action;

import com.ww.model.constant.rival.RivalStatus;
import com.ww.game.play.PlayManager;

import java.util.Map;

public class PlayAnswerAction extends PlayAction {

    public PlayAnswerAction(PlayManager manager) {
        super(manager);
    }

    @Override
    public void perform(Long profileId, Map<String, Object> content) {
        if (!getFlow().isStatusEquals(RivalStatus.ANSWERING)
                || !content.containsKey("answerId")) {
            return;
        }
        Long answerId = null;
        try {
            answerId = ((Integer) content.get("answerId")).longValue();
        } catch (Exception e) {
            e.printStackTrace();
        }
        getFlow().answeredAction(profileId, answerId);
    }
}
