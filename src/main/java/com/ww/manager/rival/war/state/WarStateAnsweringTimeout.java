package com.ww.manager.rival.war.state;

import com.ww.manager.rival.war.WarManager;
import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.container.rival.RivalTeam;
import com.ww.model.container.rival.war.WarTeam;
import io.reactivex.Flowable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class WarStateAnsweringTimeout extends WarState {

    public WarStateAnsweringTimeout(WarManager manager) {
        super(manager);
    }

    @Override
    protected Flowable<Long> processFlowable() {
        if (manager.getModel().getStatus() != RivalStatus.ANSWERING) {
            return Flowable.empty();
        }
        manager.getModel().stopWisieAnswerManager();
        for (RivalTeam profileContainer : manager.getModel().getTeamsContainer().getTeamContainers()) {
            WarTeam warProfileContainer = (WarTeam) profileContainer;
            warProfileContainer.setActiveTeamMemberPresentToFalse();
        }
        manager.getModel().setStatus(RivalStatus.ANSWERING_TIMEOUT);
        manager.getModel().getTeamsContainer().forEachProfile(profileContainer -> {
            Map<String, Object> model = new HashMap<>();
            manager.getModelFactory().fillModelAnsweringTimeout(model, profileContainer);
            manager.send(model, manager.getMessageContent(), profileContainer.getProfileId());
        });
        return Flowable.intervalRange(0L, 1L, manager.getInterval().getAnsweringTimeoutInterval(), manager.getInterval().getAnsweringTimeoutInterval(), TimeUnit.MILLISECONDS);
    }
}
