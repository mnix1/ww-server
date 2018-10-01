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
        manager.getContainer().setStatus(RivalStatus.ANSWERED);
        manager.getContainer().setAnsweredProfileId(profileId);
        Boolean isAnswerCorrect = false;
        if (content.containsKey("answerId")) {
            Long markedAnswerId = ((Integer) content.get("answerId")).longValue();
            manager.getContainer().setMarkedAnswerId(markedAnswerId);
            isAnswerCorrect = manager.getContainer().findCurrentCorrectAnswerId().equals(markedAnswerId);
        }
        BattleProfileContainer container = (BattleProfileContainer) manager.getContainer().getTeamsContainer().profileContainer(profileId);
        container.setScore(isAnswerCorrect ? container.getScore() + manager.getContainer().getCurrentTaskPoints() : container.getScore() - manager.getContainer().getCurrentTaskPoints());
        manager.getContainer().getTeamsContainer().forEachProfile(profileContainer -> {
            Map<String, Object> model = new HashMap<>();
            manager.getModelFactory().fillModelAnswered(model, profileContainer);
            manager.send(model, manager.getMessageContent(), profileContainer.getProfileId());
        });
        return Flowable.intervalRange(0L, 1L, manager.getInterval().getShowingAnswerInterval(), manager.getInterval().getShowingAnswerInterval(), TimeUnit.MILLISECONDS);
    }
}
