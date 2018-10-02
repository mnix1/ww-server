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
        super(manager);
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
        WarTeam profileContainer = manager.getModel().getTeams().team(profileId);
        if (manager.getModel().isOpponent()) {
            if (isAnswerCorrect) {
                profileContainer = manager.getModel().getTeams().opponentTeam(profileContainer.getProfileId());
            }
            profileContainer.setActiveTeamMemberPresentToFalse();
        } else if (!isAnswerCorrect) {
            profileContainer.setActiveTeamMemberPresentToFalse();
        }
        manager.getModel().getTeams().forEachTeam(pC -> {
            Map<String, Object> model = new HashMap<>();
            manager.getModelFactory().fillModelAnswered(model, pC);
            manager.send(model, manager.getMessageContent(), pC.getProfileId());
        });
        return Flowable.intervalRange(0L, 1L, manager.getInterval().getShowingAnswerInterval(), manager.getInterval().getShowingAnswerInterval(), TimeUnit.MILLISECONDS);
    }
}
