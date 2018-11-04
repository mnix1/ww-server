package com.ww.game.play.state.campaign;

import com.ww.game.play.PlayManager;
import com.ww.game.play.command.campaign.PlayCampaignSetDefaultActiveIndexCommand;
import com.ww.game.play.container.PlayContainer;
import com.ww.game.play.state.war.PlayWarChoosingWhoAnswerState;

public class PlayCampaignChoosingWhoAnswerState extends PlayWarChoosingWhoAnswerState {
    public PlayCampaignChoosingWhoAnswerState(PlayManager manager) {
        super(manager);
    }

    @Override
    protected void initSetDefaultActiveIndexCommand() {
        commands.add(new PlayCampaignSetDefaultActiveIndexCommand(getContainer()));
    }
}
