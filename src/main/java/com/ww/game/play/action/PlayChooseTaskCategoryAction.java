package com.ww.game.play.action;

import com.ww.model.constant.Category;
import com.ww.model.constant.rival.RivalStatus;
import com.ww.game.play.PlayManager;

import java.util.Map;

public class PlayChooseTaskCategoryAction extends PlayAction {

    public PlayChooseTaskCategoryAction(PlayManager manager) {
        super(manager);
    }

    @Override
    public void perform(Long profileId, Map<String, Object> content) {
        if (!flow.isStatusEquals(RivalStatus.CHOOSING_TASK_CATEGORY)
                || !content.containsKey("category")
                || !profileId.equals(container.findChoosingTaskPropsProfile().getId())) {
            return;
        }
        Category category = Category.RANDOM;
        try {
            category = Category.fromString((String) content.get("category"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        flow.chosenTaskCategoryAction(category);
    }
}
