package com.ww.manager.rival.state;

import com.ww.manager.rival.RivalManager;
import com.ww.model.constant.Category;
import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.constant.rival.DifficultyLevel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class StateChosenTaskProps extends State {
    protected static final Logger logger = LoggerFactory.getLogger(StateChosenTaskProps.class);

    private Long profileId;
    private Map<String, Object> content;

    public StateChosenTaskProps(RivalManager manager, Long profileId, Map<String, Object> content) {
        super(manager, STATE_TYPE_DECISION);
        this.profileId = profileId;
        this.content = content;
    }

    @Override
    protected Boolean processBoolean() {
        if (!manager.getTeam(profileId).getProfile().getTag().equals(manager.getModel().findChoosingTaskPropsTag())) {
            logger.error("Not choosing profile tried to choose task props, profileId: {}", profileId);
            return false;
        }
        Category category = null;
        DifficultyLevel difficultyLevel = null;
        try {
            if (content.containsKey("category")) {
                category = Category.fromString((String) content.get("category"));
            }
            if (content.containsKey("difficultyLevel")) {
                difficultyLevel = DifficultyLevel.fromString((String) content.get("difficultyLevel"));
            }
        } catch (Exception e) {
            logger.error("Wrong content on stateChosenTaskProps for profileId: {}", profileId);
        }
        if (!manager.getModel().getIsChosenDifficulty() && difficultyLevel != null) {
            manager.getModel().setIsChosenDifficulty(true);
            manager.getModel().setChosenDifficulty(difficultyLevel);
        }
        if (!manager.getModel().getIsChosenCategory() && category != null) {
            manager.getModel().setIsChosenCategory(true);
            manager.getModel().setChosenCategory(category);
        }
        if (!manager.getModel().getIsChosenDifficulty() || !manager.getModel().getIsChosenCategory()) {
            return false;
        }
        manager.getModel().setStatus(RivalStatus.CHOSEN_TASK_PROPS);
        manager.prepareTask((long) manager.getModel().getCurrentTaskIndex() + 1, manager.getModel().getChosenCategory(), manager.getModel().getChosenDifficulty());
        return true;
    }
}
