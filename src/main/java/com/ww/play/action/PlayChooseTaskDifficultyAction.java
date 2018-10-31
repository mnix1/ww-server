package com.ww.play.action;

import com.ww.model.constant.rival.DifficultyLevel;
import com.ww.model.constant.rival.RivalStatus;
import com.ww.play.PlayManager;

import java.util.Map;

public class PlayChooseTaskDifficultyAction extends PlayAction {

    public PlayChooseTaskDifficultyAction(PlayManager manager) {
        super(manager);
    }

    @Override
    public void perform(Long profileId, Map<String, Object> content) {
        if (!container.isStatusEquals(RivalStatus.CHOOSING_TASK_DIFFICULTY)
                || !content.containsKey("difficultyLevel")
                || !profileId.equals(container.findChoosingTaskPropsProfile().getId())) {
            return;
        }
        DifficultyLevel difficultyLevel = DifficultyLevel.NORMAL;
        try {
            difficultyLevel = DifficultyLevel.fromString((String) content.get("difficultyLevel"));
        } catch (Exception e) {
        }
        flow.chosenTaskDifficultyAction(difficultyLevel);
    }
}
