package com.ww.play.state.campaign;

import com.ww.play.command.campaign.PlayCampaignSetDefaultActiveIndexCommand;
import com.ww.play.container.PlayContainer;
import com.ww.play.state.war.PlayWarChoosingWhoAnswerState;

public class PlayCampaignChoosingWhoAnswerState extends PlayWarChoosingWhoAnswerState {
    public PlayCampaignChoosingWhoAnswerState(PlayContainer container, long interval) {
        super(container, interval);
    }

    protected void initSetDefaultActiveIndexCommand() {
        commands.add(new PlayCampaignSetDefaultActiveIndexCommand(container));
    }
}
