package com.ww.play.state.war;

import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.war.WarTeam;
import com.ww.play.command.PlayAnswerCommand;
import com.ww.play.command.war.PlayDisableActiveTeamMemberCommand;
import com.ww.play.container.PlayContainer;
import com.ww.play.state.PlayAnsweredState;

import java.util.Map;

import static com.ww.play.modelfiller.PlayModelFiller.fillModelAnswered;
import static com.ww.play.modelfiller.PlayWarModelFiller.fillModelPresentIndexes;

public class PlayWarAnsweredState extends PlayAnsweredState {

    public PlayWarAnsweredState(PlayContainer container, Long profileId, Long answerId) {
        super(container, profileId, answerId);
    }

    @Override
    public void initCommands() {
        super.initCommands();
        boolean isCorrect = container.getTasks().correctAnswerId().equals(answerId);
        Long commandProfileId = isCorrect ? container.getTeams().opponent(profileId).getProfileId() : profileId;
        commands.add(new PlayDisableActiveTeamMemberCommand(container, commandProfileId));
    }

    @Override
    public Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam) {
        Map<String, Object> model = super.prepareModel(team, opponentTeam);
        fillModelPresentIndexes(model, (WarTeam) team, (WarTeam) opponentTeam);
        return model;
    }
}
