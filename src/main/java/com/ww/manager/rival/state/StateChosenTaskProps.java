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
        super(manager);
        this.profileId = profileId;
        this.content = content;
    }

    @Override
    protected Boolean processBoolean() {
        if (!manager.getContainer().getTeamsContainer().profileContainer(profileId).getProfile().getTag().equals(manager.getContainer().findChoosingTaskPropsTag())) {
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
        if (!manager.getContainer().getIsChosenDifficulty() && difficultyLevel != null) {
            manager.getContainer().setIsChosenDifficulty(true);
            manager.getContainer().setChosenDifficulty(difficultyLevel);
        }
        if (!manager.getContainer().getIsChosenCategory() && category != null) {
            manager.getContainer().setIsChosenCategory(true);
            manager.getContainer().setChosenCategory(category);
        }
        if (!manager.getContainer().getIsChosenDifficulty() || !manager.getContainer().getIsChosenCategory()) {
            return false;
        }
        manager.getContainer().setStatus(RivalStatus.CHOSEN_TASK_PROPS);
        manager.prepareTask((long) manager.getContainer().getCurrentTaskIndex() + 1, manager.getContainer().getChosenCategory(), manager.getContainer().getChosenDifficulty());
        return true;
    }
}
