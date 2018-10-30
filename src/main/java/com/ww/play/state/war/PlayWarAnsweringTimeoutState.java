package com.ww.play.state.war;

import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.war.WarTeam;
import com.ww.play.command.war.PlayDisableActiveTeamMemberCommand;
import com.ww.play.container.PlayContainer;
import com.ww.play.state.PlayAnsweringTimeoutState;

import java.util.Map;

import static com.ww.play.modelfiller.PlayWarModelFiller.fillModelPresentIndexes;

public class PlayWarAnsweringTimeoutState extends PlayAnsweringTimeoutState {

    public PlayWarAnsweringTimeoutState(PlayContainer container) {
        super(container);
    }

    @Override
    public void initCommands() {
        super.initCommands();
        for (RivalTeam team : container.getTeams().getTeams()) {
            commands.add(new PlayDisableActiveTeamMemberCommand(container, team.getProfileId()));
        }
    }

    @Override
    public Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam) {
        Map<String, Object> model = super.prepareModel(team, opponentTeam);
        fillModelPresentIndexes(model, (WarTeam) team, (WarTeam) opponentTeam);
        return model;
    }
}
