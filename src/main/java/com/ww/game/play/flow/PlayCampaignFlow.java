package com.ww.game.play.flow;

import com.ww.model.container.rival.RivalInterval;
import com.ww.model.container.rival.war.WarInterval;
import com.ww.game.play.PlayManager;
import com.ww.game.play.state.*;
import com.ww.game.play.state.campaign.PlayCampaignChoosingWhoAnswerState;
import com.ww.game.play.state.war.*;

public class PlayCampaignFlow extends PlayWarFlow {

    public PlayCampaignFlow(PlayManager manager, RivalInterval interval) {
        super(manager, interval);
    }

    @Override
    protected synchronized PlayState createChoosingWhoAnswerState() {
        return new PlayCampaignChoosingWhoAnswerState(getContainer(), ((WarInterval) interval).getChoosingWhoAnswerInterval());
    }
}
