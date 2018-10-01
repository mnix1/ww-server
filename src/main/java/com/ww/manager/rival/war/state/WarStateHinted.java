package com.ww.manager.rival.war.state;

import com.ww.manager.rival.war.WarManager;
import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.model.constant.rival.RivalStatus;
import com.ww.model.container.rival.war.WarProfileContainer;
import io.reactivex.Flowable;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class WarStateHinted extends WarState {

    private Long profileId;
    private Map<String, Object> content;

    public WarStateHinted(WarManager manager, Long profileId, Map<String, Object> content) {
        super(manager);
        this.profileId = profileId;
        this.content = content;
    }

    @Override
    protected void processVoid() {
        WarProfileContainer container = (WarProfileContainer) rivalContainer.getProfileIdRivalProfileContainerMap().get(profileId);
        WisieAnswerManager wisieAnswerManager = warManager.warContainer.getWisieAnswerManager(profileId);
        if (container.getHints() <= 0 || !content.containsKey("answerId") || wisieAnswerManager == null) {
            return;
        }
        container.decreaseHints();
        Long markedAnswerId = ((Integer) content.get("answerId")).longValue();
        Boolean isAnswerCorrect = rivalContainer.findCurrentCorrectAnswerId().equals(markedAnswerId);
        wisieAnswerManager.hint(markedAnswerId, isAnswerCorrect);
    }
}
