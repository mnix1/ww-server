package com.ww.manager.rival.war.state;

import com.ww.manager.rival.war.WarManager;
import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.container.rival.RivalProfileContainer;
import com.ww.model.container.rival.war.WarProfileContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class WarStateChosenWhoAnswer extends WarState {
    protected static final Logger logger = LoggerFactory.getLogger(WarStateChosenWhoAnswer.class);

    private Long profileId;
    private Map<String, Object> content;

    public WarStateChosenWhoAnswer(WarManager manager, Long profileId, Map<String, Object> content) {
        super(manager);
        this.profileId = profileId;
        this.content = content;
    }

    @Override
    protected Boolean processBoolean() {
        Integer activeIndex = null;
        try {
            if (content.containsKey("activeIndex")) {
                activeIndex = (Integer) content.get("activeIndex");
            }
        } catch (Exception e) {
            logger.error("Wrong content on WarStateChosenWhoAnswer for profileId: {}", profileId);
        }
        WarProfileContainer warProfileContainer = manager.getContainer().getTeamsContainer().profileContainer(profileId);
        if (activeIndex != null && !warProfileContainer.isChosenActiveIndex() && warProfileContainer.getPresentIndexes().contains(activeIndex)) {
            warProfileContainer.setChosenActiveIndex(true);
            warProfileContainer.setActiveIndex(activeIndex);
        }
        if (!allPlayersChoosen()) {
            return false;
        }
        this.manager.getContainer().setStatus(RivalStatus.CHOSEN_WHO_ANSWER);
        return true;
    }

    protected boolean allPlayersChoosen() {
        for (RivalProfileContainer profileContainer : manager.getContainer().getTeamsContainer().getProfileContainers()) {
            WarProfileContainer warProfileContainer = (WarProfileContainer) profileContainer;
            if (!warProfileContainer.isChosenActiveIndex()) {
                return false;
            }
        }
        return true;
    }
}
