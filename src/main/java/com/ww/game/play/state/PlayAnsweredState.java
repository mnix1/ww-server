package com.ww.game.play.state;

import com.ww.game.play.PlayManager;
import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.container.rival.RivalTeam;
import com.ww.game.play.command.PlayAnswerCommand;

import java.util.Map;

import static com.ww.game.play.modelfiller.PlayModelFiller.fillModelAnswered;

public class PlayAnsweredState extends PlayState {
    protected Long profileId;
    protected Long answerId;

    public PlayAnsweredState(PlayManager manager, Long profileId, Long answerId) {
        super(manager, RivalStatus.ANSWERED);
        this.profileId = profileId;
        this.answerId = answerId;
    }

    @Override
    public void initCommands() {
        commands.add(new PlayAnswerCommand(getContainer(), profileId, answerId));
    }

    @Override
    public Map<String, Object> prepareModel(RivalTeam team, RivalTeam opponentTeam) {
        Map<String, Object> model = super.prepareModel(team, opponentTeam);
        fillModelAnswered(model, getContainer(), team);
        return model;
    }

    @Override
    public long afterInterval() {
        return manager.getInterval().getAnsweredInterval();
    }

    @Override
    public void after(){
        String stateName = "CHOOSING_TASK_CATEGORY";
        if (getContainer().isEnd()) {
            stateName = "END";
        } else if (getContainer().isRandomTaskProps()) {
            stateName = "RANDOM_TASK_PROPS";
        }
        manager.getFlow().run(stateName);
    }
}
