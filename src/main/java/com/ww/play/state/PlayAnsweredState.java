package com.ww.play.state;

import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.container.rival.RivalTeam;
import com.ww.play.command.PlayAnswerCommand;
import com.ww.play.command.PlaySetNextTimeoutCommand;
import com.ww.play.container.PlayContainer;

import java.util.Map;

import static com.ww.play.modelfiller.PlayModelFiller.fillModelAnswered;
import static com.ww.play.modelfiller.PlayModelFiller.fillModelNextInterval;
import static com.ww.play.modelfiller.PlayModelFiller.fillModelTask;

public class PlayAnsweredState extends PlayState {
    protected Long profileId;
    protected Long answerId;

    public PlayAnsweredState(PlayContainer container, Long profileId, Long answerId) {
        super(container, RivalStatus.ANSWERED);
        this.profileId = profileId;
        this.answerId = answerId;
    }

    @Override
    public void initCommands() {
        commands.add(new PlayAnswerCommand(container, profileId, answerId));
    }

    @Override
    public Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam) {
        Map<String, Object> model = super.prepareModel(team, opponentTeam);
        fillModelAnswered(model, container, team);
        return model;
    }
}
