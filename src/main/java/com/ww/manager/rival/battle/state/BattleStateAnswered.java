package com.ww.manager.rival.battle.state;

import com.ww.manager.rival.RivalManager;
import com.ww.manager.rival.state.State;
import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.container.rival.battle.BattleProfileContainer;
import io.reactivex.Flowable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class BattleStateAnswered extends State {

    private Long profileId;
    private Map<String, Object> content;

    public BattleStateAnswered(RivalManager manager, Long profileId, Map<String, Object> content) {
        super(manager);
        this.profileId = profileId;
        this.content = content;
    }

    @Override
    protected Flowable<Long> processFlowable() {
        rivalContainer.setStatus(RivalStatus.ANSWERED);
        rivalContainer.setAnsweredProfileId(profileId);
        Boolean isAnswerCorrect = false;
        if (content.containsKey("answerId")) {
            Long markedAnswerId = ((Integer) content.get("answerId")).longValue();
            rivalContainer.setMarkedAnswerId(markedAnswerId);
            isAnswerCorrect = rivalContainer.findCurrentCorrectAnswerId().equals(markedAnswerId);
        }
        BattleProfileContainer container = (BattleProfileContainer) rivalContainer.getProfileIdRivalProfileContainerMap().get(profileId);
        container.setScore(isAnswerCorrect ? container.getScore() + rivalContainer.getCurrentTaskPoints() : container.getScore() - rivalContainer.getCurrentTaskPoints());
        rivalContainer.forEachProfile(rivalProfileContainer -> {
            Map<String, Object> model = new HashMap<>();
            rivalContainer.fillModelAnswered(model, rivalProfileContainer);
            rivalManager.send(model, rivalManager.getMessageContent(), rivalProfileContainer.getProfileId());
        });
        return Flowable.intervalRange(0L, 1L, rivalManager.getShowingAnswerInterval(), rivalManager.getShowingAnswerInterval(), TimeUnit.MILLISECONDS);
    }
}
