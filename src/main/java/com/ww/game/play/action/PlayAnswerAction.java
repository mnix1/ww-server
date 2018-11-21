package com.ww.game.play.action;

import com.ww.game.play.PlayManager;
import com.ww.model.constant.rival.RivalStatus;

import java.util.Map;

public class PlayAnswerAction extends PlayAction {

    public PlayAnswerAction(PlayManager manager) {
        super(manager);
    }

    @Override
    public void perform(Long profileId, Map<String, Object> content) {
        if (!correct(profileId, content)) {
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

    protected boolean correct(Long profileId, Map<String, Object> content) {
        return getFlow().isStatusEquals(RivalStatus.ANSWERING)
                && content.containsKey("answerId");
    }
}
