package com.ww.manager.rival.war.state;

import com.ww.manager.rival.war.WarManager;
import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.container.rival.war.WarTeam;
import io.reactivex.Flowable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class WarStateAnswered extends WarState {
    protected Long profileId;
    protected Map<String, Object> content;
    protected Boolean isAnswerCorrect = false;

    public WarStateAnswered(WarManager manager, Long profileId, Map<String, Object> content) {
        super(manager, STATE_TYPE_FLOWABLE);
        this.profileId = profileId;
        this.content = content;
    }

    protected void updateTeamPresent() {
        WarTeam team = (WarTeam) manager.getTeam(profileId);
        if (manager.getModel().isOpponent()) {
            if (isAnswerCorrect) {
                team = (WarTeam) manager.getModel().getTeams().opponent(team.getProfileId());
            }
            team.setActiveTeamMemberPresentToFalse();
        } else if (!isAnswerCorrect) {
            team.setActiveTeamMemberPresentToFalse();
        }
    }

    @Override
    protected Flowable<Long> processFlowable() {
        manager.getModel().setStatus(RivalStatus.ANSWERED);
        manager.getModel().stopWisieAnswerManager();
        manager.getModel().setAnsweredProfileId(profileId);
        if (content.containsKey("answerId")) {
            Long markedAnswerId = ((Integer) content.get("answerId")).longValue();
            manager.getModel().setMarkedAnswerId(markedAnswerId);
            isAnswerCorrect = manager.getModel().findCurrentCorrectAnswerId().equals(markedAnswerId);
        }
        updateTeamPresent();
        manager.getModel().getTeams().forEachTeam(rivalTeam -> {
            Map<String, Object> model = new HashMap<>();
            manager.getModelFactory().fillModelAnswered(model, rivalTeam);
            manager.send(model, manager.getMessageContent(), rivalTeam.getProfileId());
        });
        return Flowable.intervalRange(0L, 1L, manager.getInterval().getAnsweredInterval(), manager.getInterval().getAnsweredInterval(), TimeUnit.MILLISECONDS);
    }
}
