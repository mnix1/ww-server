package com.ww.game.play.state.war;

import com.ww.game.play.PlayManager;
import com.ww.game.play.command.war.PlayWarDisableActiveTeamMemberCommand;
import com.ww.game.play.command.war.PlayWarStopActiveMemberManagerFlowsCommand;
import com.ww.game.play.state.PlayAnsweringTimeoutState;
import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.war.WarTeam;

import java.util.Map;

import static com.ww.game.play.modelfiller.PlayWarModelFiller.fillModelPresentIndexes;

public class PlayWarAnsweringTimeoutState extends PlayAnsweringTimeoutState {

    public PlayWarAnsweringTimeoutState(PlayManager manager) {
        super(manager);
    }

    @Override
    public void initCommands() {
        super.initCommands();
        for (RivalTeam team : getContainer().getTeams().getTeams()) {
            commands.add(new PlayWarDisableActiveTeamMemberCommand(getContainer(), team.getProfileId()));
        }
        commands.add(new PlayWarStopActiveMemberManagerFlowsCommand(getContainer()));
    }

    @Override
    public Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam) {
        Map<String, Object> model = super.prepareModel(team, opponentTeam);
        fillModelPresentIndexes(model, (WarTeam) team, (WarTeam) opponentTeam);
        return model;
    }
}
