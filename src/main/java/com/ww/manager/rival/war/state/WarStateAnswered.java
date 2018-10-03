package com.ww.manager.rival.war.state;

import com.ww.manager.rival.war.WarManager;
import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.container.rival.war.WarTeam;
import io.reactivex.Flowable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class WarStateAnswered extends WarState {

    private Long profileId;
    private Map<String, Object> content;

    public WarStateAnswered(WarManager manager, Long profileId, Map<String, Object> content) {
        super(manager, STATE_TYPE_FLOWABLE);
        this.profileId = profileId;
        this.content = content;
    }

    @Override
    protected Flowable<Long> processFlowable() {
        manager.getModel().setStatus(RivalStatus.ANSWERED);
        manager.getModel().stopWisieAnswerManager();
        manager.getModel().setAnsweredProfileId(profileId);
        Boolean isAnswerCorrect = false;
        if (content.containsKey("answerId")) {
            Long markedAnswerId = ((Integer) content.get("answerId")).longValue();
            manager.getModel().setMarkedAnswerId(markedAnswerId);
            isAnswerCorrect = manager.getModel().findCurrentCorrectAnswerId().equals(markedAnswerId);
        }
        WarTeam team = manager.getModel().getTeams().team(profileId);
        if (manager.getModel().isOpponent()) {
            if (isAnswerCorrect) {
                team = manager.getModel().getTeams().opponentTeam(team.getProfileId());
            }
            team.setActiveTeamMemberPresentToFalse();
        } else if (!isAnswerCorrect) {
            team.setActiveTeamMemberPresentToFalse();
        }
        manager.getModel().getTeams().forEachTeam(rivalTeam -> {
            Map<String, Object> model = new HashMap<>();
            manager.getModelFactory().fillModelAnswered(model, rivalTeam);
            manager.send(model, manager.getMessageContent(), rivalTeam.getProfileId());
        });
        return Flowable.intervalRange(0L, 1L, manager.getInterval().getShowingAnswerInterval(), manager.getInterval().getShowingAnswerInterval(), TimeUnit.MILLISECONDS);
    }
}
