package com.ww.manager.rival.campaign.state;

import com.ww.manager.rival.campaign.CampaignWarManager;
import com.ww.manager.rival.war.state.WarStateChosenWhoAnswer;
import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.war.WarTeam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static com.ww.manager.rival.campaign.CampaignWarManager.BOT_PROFILE_ID;

public class CampaignWarStateChosenWhoAnswer extends WarStateChosenWhoAnswer {
    protected static final Logger logger = LoggerFactory.getLogger(CampaignWarStateChosenWhoAnswer.class);

    public CampaignWarStateChosenWhoAnswer(CampaignWarManager manager, Long profileId, Map<String, Object> content) {
        super(manager, profileId, content);
    }

    protected boolean allPlayersChoosen() {
        for (RivalTeam profileContainer : manager.getModel().getTeams().getTeams()) {
            WarTeam warProfileContainer = (WarTeam) profileContainer;
            if (!warProfileContainer.isChosenActiveIndex() && !warProfileContainer.getProfileId().equals(BOT_PROFILE_ID)) {
                return false;
            }
        }
        return true;
    }
}
