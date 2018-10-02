package com.ww.manager.rival.war.state;

import com.ww.manager.rival.war.WarManager;
import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.container.rival.RivalTeamContainer;
import com.ww.model.container.rival.war.WarTeamContainer;
import io.reactivex.Flowable;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class WarStateChoosingWhoAnswer extends WarState {

    public WarStateChoosingWhoAnswer(WarManager manager) {
        super(manager);
    }

    @Override
    protected Flowable<Long> processFlowable() {
        this.manager.getContainer().setStatus(RivalStatus.CHOOSING_WHO_ANSWER);

        for (RivalTeamContainer profileContainer : manager.getContainer().getTeamsContainer().getTeamContainers()) {
            WarTeamContainer warProfileContainer = (WarTeamContainer) profileContainer;
            warProfileContainer.setActiveIndex(warProfileContainer.getPresentIndexes().get(0));
            warProfileContainer.setChosenActiveIndex(false);
        }

        int interval = manager.getInterval().getChoosingWhoAnswerInterval();
        manager.getContainer().setEndChoosingWhoAnswerDate(Instant.now().plus(interval, ChronoUnit.MILLIS));
        manager.getContainer().getTeamsContainer().forEachProfile(profileContainer -> {
            Map<String, Object> model = new HashMap<>();
            manager.getModelFactory().fillModelChoosingWhoAnswer(model, profileContainer);
            this.manager.send(model, this.manager.getMessageContent(), profileContainer.getProfileId());
        });
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
