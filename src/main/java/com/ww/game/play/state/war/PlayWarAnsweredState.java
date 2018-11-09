package com.ww.game.play.state.war;

import com.ww.game.play.PlayManager;
import com.ww.game.play.command.war.PlayWarStopActiveMemberManagerFlowsCommand;
import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.war.WarTeam;
import com.ww.game.play.command.war.PlayWarDisableActiveTeamMemberCommand;
import com.ww.game.play.container.PlayContainer;
import com.ww.game.play.state.PlayAnsweredState;

import java.util.Map;

import static com.ww.game.play.modelfiller.PlayWarModelFiller.fillModelPresentIndexes;

public class PlayWarAnsweredState extends PlayAnsweredState {
    protected boolean isCorrect;

    public PlayWarAnsweredState(PlayManager manager, Long profileId, Long answerId) {
        super(manager, profileId, answerId);
        isCorrect = getContainer().getTasks().correctAnswerId().equals(answerId);
    }

    @Override
    public void initCommands() {
        super.initCommands();
        Long commandProfileId = isCorrect ? getContainer().getTeams().opponent(profileId).getProfileId() : profileId;
        commands.add(new PlayWarDisableActiveTeamMemberCommand(getContainer(), commandProfileId));
        commands.add(new PlayWarStopActiveMemberManagerFlowsCommand(getContainer()));
    }

    @Override
    public Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam) {
        Map<String, Object> model = super.prepareModel(team, opponentTeam);
        fillModelPresentIndexes(model, (WarTeam) team, (WarTeam) opponentTeam);
        return model;
    }
}
