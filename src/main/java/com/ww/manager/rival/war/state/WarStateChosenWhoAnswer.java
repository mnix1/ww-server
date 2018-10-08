package com.ww.manager.rival.war.state;

import com.ww.manager.rival.war.WarManager;
import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.war.WarTeam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class WarStateChosenWhoAnswer extends WarState {
    protected static final Logger logger = LoggerFactory.getLogger(WarStateChosenWhoAnswer.class);

    private Long profileId;
    private Map<String, Object> content;

    public WarStateChosenWhoAnswer(WarManager manager, Long profileId, Map<String, Object> content) {
        super(manager, STATE_TYPE_DECISION);
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
        WarTeam warTeam =(WarTeam)  manager.getTeam(profileId);
        if (activeIndex != null && !warTeam.isChosenActiveIndex() && warTeam.getPresentIndexes().contains(activeIndex)) {
            warTeam.setChosenActiveIndex(true);
            warTeam.setActiveIndex(activeIndex);
        }
        if (!allPlayersChoosen()) {
            return false;
        }
        this.manager.getModel().setStatus(RivalStatus.CHOSEN_WHO_ANSWER);
        return true;
    }

    protected boolean allPlayersChoosen() {
        for (RivalTeam team : manager.getModel().getTeams().getTeams()) {
            WarTeam warTeam = (WarTeam) team;
            if (!warTeam.isChosenActiveIndex()) {
                return false;
            }
        }
        return true;
    }
}
