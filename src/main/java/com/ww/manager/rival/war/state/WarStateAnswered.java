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
        rivalContainer.setStatus(RivalStatus.ANSWERED);
        warManager.warContainer.stopWisieAnswerManager();
        rivalContainer.setAnsweredProfileId(profileId);
        Boolean isAnswerCorrect = false;
        if (content.containsKey("answerId")) {
            Long markedAnswerId = ((Integer) content.get("answerId")).longValue();
            rivalContainer.setMarkedAnswerId(markedAnswerId);
            isAnswerCorrect = rivalContainer.findCurrentCorrectAnswerId().equals(markedAnswerId);
        }
        WarProfileContainer profileContainer = (WarProfileContainer) rivalContainer.profileContainer(profileId);
        if (rivalContainer.isOpponent()) {
            if (isAnswerCorrect) {
                profileContainer = (WarProfileContainer) rivalContainer.opponentProfileContainer(profileContainer.getProfileId());
            }
            profileContainer.setActiveTeamMemberPresentToFalse();
        } else if (!isAnswerCorrect) {
            profileContainer.setActiveTeamMemberPresentToFalse();
        }
        rivalContainer.forEachProfile(rivalProfileContainer -> {
            Map<String, Object> model = new HashMap<>();
            rivalManager.getModelFactory().fillModelAnswered(model, rivalProfileContainer);
            warManager.send(model, warManager.getMessageContent(), rivalProfileContainer.getProfileId());
        });
        return Flowable.intervalRange(0L, 1L, warManager.getShowingAnswerInterval(), warManager.getShowingAnswerInterval(), TimeUnit.MILLISECONDS);
    }
}
