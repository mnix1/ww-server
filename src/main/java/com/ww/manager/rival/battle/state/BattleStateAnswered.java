package com.ww.manager.rival.battle.state;

import com.ww.manager.rival.RivalManager;
import com.ww.manager.rival.state.State;
import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.container.rival.battle.BattleTeam;
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
        manager.getModel().setStatus(RivalStatus.ANSWERED);
        manager.getModel().setAnsweredProfileId(profileId);
        Boolean isAnswerCorrect = false;
        if (content.containsKey("answerId")) {
            Long markedAnswerId = ((Integer) content.get("answerId")).longValue();
            manager.getModel().setMarkedAnswerId(markedAnswerId);
            isAnswerCorrect = manager.getModel().findCurrentCorrectAnswerId().equals(markedAnswerId);
        }
        BattleTeam container = (BattleTeam) manager.getModel().getTeamsContainer().teamContainer(profileId);
        container.setScore(isAnswerCorrect ? container.getScore() + manager.getModel().getCurrentTaskPoints() : container.getScore() - manager.getModel().getCurrentTaskPoints());
        manager.getModel().getTeamsContainer().forEachProfile(profileContainer -> {
            Map<String, Object> model = new HashMap<>();
            manager.getModelFactory().fillModelAnswered(model, profileContainer);
            manager.send(model, manager.getMessageContent(), profileContainer.getProfileId());
        });
        return Flowable.intervalRange(0L, 1L, manager.getInterval().getShowingAnswerInterval(), manager.getInterval().getShowingAnswerInterval(), TimeUnit.MILLISECONDS);
    }
}
