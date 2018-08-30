package com.ww.manager.rival.state;

import com.ww.manager.rival.RivalManager;
import com.ww.model.constant.Category;
import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.constant.rival.task.TaskDifficultyLevel;
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
        if (!rivalContainer.getRivalProfileContainer(profileId).getProfile().getTag().equals(rivalContainer.findChoosingTaskPropsTag())) {
            logger.error("Not choosing profile tried to choose task props, profileId: {}", profileId);
            return false;
        }
        rivalContainer.setStatus(RivalStatus.CHOSEN_TASK_PROPS);
        Category category = Category.random();
        TaskDifficultyLevel difficultyLevel = TaskDifficultyLevel.random();
        try {
            if (content.containsKey("category")) {
                category = Category.fromString((String) content.get("category"));
            }
            if (content.containsKey("difficultyLevel")) {
                difficultyLevel = TaskDifficultyLevel.fromString((String) content.get("difficultyLevel"));
            }
        } catch (Exception e) {
            logger.error("Wrong content on stateChosenTaskProps for profileId: {}", profileId);
        }
        rivalManager.prepareTask((long) rivalContainer.getCurrentTaskIndex() + 1, category, difficultyLevel);
        return true;
    }
}
