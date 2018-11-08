package com.ww.game.play.action;

import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.model.constant.rival.RivalStatus;
import com.ww.game.play.PlayManager;

import java.util.Map;

public class PlayChooseTaskDifficultyAction extends PlayAction {

    public PlayChooseTaskDifficultyAction(PlayManager manager) {
        super(manager);
    }

    @Override
    public void perform(Long profileId, Map<String, Object> content) {
        if (!getFlow().isStatusEquals(RivalStatus.CHOOSING_TASK_DIFFICULTY)
                || !content.containsKey("difficultyLevel")
                || !profileId.equals(getContainer().findChoosingTaskPropsProfile().getId())) {
            return;
        }
        DifficultyLevel difficultyLevel = DifficultyLevel.NORMAL;
        try {
            difficultyLevel = DifficultyLevel.fromString((String) content.get("difficultyLevel"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        getFlow().chosenTaskDifficultyAction(difficultyLevel);
    }
}
