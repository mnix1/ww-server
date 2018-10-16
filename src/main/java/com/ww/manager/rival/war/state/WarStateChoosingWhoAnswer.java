package com.ww.manager.rival.war.state;

import com.ww.manager.rival.war.WarManager;
import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.war.WarTeam;
import io.reactivex.Flowable;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class WarStateChoosingWhoAnswer extends WarState {

    public WarStateChoosingWhoAnswer(WarManager manager) {
        super(manager, STATE_TYPE_FLOWABLE);
    }

    protected void setTeamsDefaultActive(){
        for (RivalTeam team : manager.getModel().getTeams().getTeams()) {
            WarTeam warTeam = (WarTeam) team;
            warTeam.setActiveIndex(warTeam.getPresentIndexes().get(0));
            warTeam.setChosenActiveIndex(false);
        }
    }

    protected void send(){
        manager.getModel().getTeams().forEachTeam(team -> {
            Map<String, Object> model = new HashMap<>();
            manager.getModelFactory().fillModelChoosingWhoAnswer(model, team);
            manager.send(model, manager.getMessageContent(), team.getProfileId());
        });
    }

    @Override
    protected Flowable<Long> processFlowable() {
        this.manager.getModel().setStatus(RivalStatus.CHOOSING_WHO_ANSWER);
        setTeamsDefaultActive();
        long interval = manager.getInterval().getChoosingWhoAnswerInterval();
        manager.getModel().setEndChoosingWhoAnswerDate(Instant.now().plus(interval, ChronoUnit.MILLIS));
        send();
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
