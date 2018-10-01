package com.ww.manager.rival.war.state;

import com.ww.manager.rival.war.WarManager;
import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.container.rival.war.WarProfileContainer;
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
        manager.getContainer().setStatus(RivalStatus.ANSWERED);
        manager.getContainer().stopWisieAnswerManager();
        manager.getContainer().setAnsweredProfileId(profileId);
        Boolean isAnswerCorrect = false;
        if (content.containsKey("answerId")) {
            Long markedAnswerId = ((Integer) content.get("answerId")).longValue();
            manager.getContainer().setMarkedAnswerId(markedAnswerId);
            isAnswerCorrect = manager.getContainer().findCurrentCorrectAnswerId().equals(markedAnswerId);
        }
        WarProfileContainer profileContainer = manager.getContainer().getTeamsContainer().profileContainer(profileId);
        if (manager.getContainer().isOpponent()) {
            if (isAnswerCorrect) {
                profileContainer = manager.getContainer().getTeamsContainer().opponentProfileContainer(profileContainer.getProfileId());
            }
            profileContainer.setActiveTeamMemberPresentToFalse();
        } else if (!isAnswerCorrect) {
            profileContainer.setActiveTeamMemberPresentToFalse();
        }
        manager.getContainer().getTeamsContainer().forEachProfile(pC -> {
            Map<String, Object> model = new HashMap<>();
            manager.getModelFactory().fillModelAnswered(model, pC);
            manager.send(model, manager.getMessageContent(), pC.getProfileId());
        });
        return Flowable.intervalRange(0L, 1L, manager.getInterval().getShowingAnswerInterval(), manager.getInterval().getShowingAnswerInterval(), TimeUnit.MILLISECONDS);
    }
}
