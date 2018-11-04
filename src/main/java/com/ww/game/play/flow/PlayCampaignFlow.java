package com.ww.game.play.flow;

import com.ww.game.play.PlayManager;
import com.ww.game.play.state.PlayState;
import com.ww.game.play.state.campaign.PlayCampaignChoosingWhoAnswerState;

public class PlayCampaignFlow extends PlayWarFlow {

    public PlayCampaignFlow(PlayManager manager) {
        super(manager);
    }

    @Override
    protected synchronized PlayState createChoosingWhoAnswerState() {
        return new PlayCampaignChoosingWhoAnswerState(manager);
    }
}
