package com.ww.game.play.state.war;

import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.war.WarTeam;
import com.ww.game.play.command.war.PlayWarDisableActiveTeamMemberCommand;
import com.ww.game.play.container.PlayContainer;
import com.ww.game.play.state.PlayAnsweringTimeoutState;

import java.util.Map;

import static com.ww.game.play.modelfiller.PlayWarModelFiller.fillModelPresentIndexes;

public class PlayWarAnsweringTimeoutState extends PlayAnsweringTimeoutState {

    public PlayWarAnsweringTimeoutState(PlayContainer container) {
        super(container);
    }

    @Override
    public void initCommands() {
        super.initCommands();
        for (RivalTeam team : container.getTeams().getTeams()) {
            commands.add(new PlayWarDisableActiveTeamMemberCommand(container, team.getProfileId()));
        }
    }

    @Override
    public Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam) {
        Map<String, Object> model = super.prepareModel(team, opponentTeam);
        fillModelPresentIndexes(model, (WarTeam) team, (WarTeam) opponentTeam);
        return model;
    }
}
