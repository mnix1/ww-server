package com.ww.play.flow;

import com.ww.model.container.rival.RivalInterval;
import com.ww.model.container.rival.war.WarInterval;
import com.ww.play.PlayManager;
import com.ww.play.state.*;
import com.ww.play.state.campaign.PlayCampaignChoosingWhoAnswerState;
import com.ww.play.state.war.*;

public class PlayCampaignFlow extends PlayWarFlow {

    public PlayCampaignFlow(PlayManager manager, RivalInterval interval) {
        super(manager, interval);
    }

    @Override
    protected PlayState createChoosingWhoAnswerState() {
        return new PlayCampaignChoosingWhoAnswerState(getContainer(), ((WarInterval) interval).getChoosingWhoAnswerInterval());
    }
}
