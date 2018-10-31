package com.ww.game.play.state.war;

import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.war.WarTeam;
import com.ww.game.play.command.war.PlayWarDisableActiveTeamMemberCommand;
import com.ww.game.play.container.PlayContainer;
import com.ww.game.play.state.PlayAnsweredState;

import java.util.Map;

import static com.ww.game.play.modelfiller.PlayWarModelFiller.fillModelPresentIndexes;

public class PlayWarAnsweredState extends PlayAnsweredState {
    protected boolean isCorrect;

    public PlayWarAnsweredState(PlayContainer container, Long profileId, Long answerId) {
        super(container, profileId, answerId);
        isCorrect = container.getTasks().correctAnswerId().equals(answerId);
    }

    @Override
    public void initCommands() {
        super.initCommands();
        Long commandProfileId = isCorrect ? container.getTeams().opponent(profileId).getProfileId() : profileId;
        commands.add(new PlayWarDisableActiveTeamMemberCommand(container, commandProfileId));
    }

    @Override
    public Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam) {
        Map<String, Object> model = super.prepareModel(team, opponentTeam);
        fillModelPresentIndexes(model, (WarTeam) team, (WarTeam) opponentTeam);
        return model;
    }
}
