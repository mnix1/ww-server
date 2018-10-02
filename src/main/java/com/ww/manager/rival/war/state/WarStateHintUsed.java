package com.ww.manager.rival.war.state;

import com.ww.manager.rival.war.WarManager;
import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.model.container.rival.war.WarProfileContainer;

import java.util.Map;

public class WarStateHintUsed extends WarState {

    private Long profileId;
    private Map<String, Object> content;

    public WarStateHintUsed(WarManager manager, Long profileId, Map<String, Object> content) {
        super(manager);
        this.profileId = profileId;
        this.content = content;
    }

    @Override
    protected void processVoid() {
        WarProfileContainer container = manager.getContainer().getTeamsContainer().profileContainer(profileId);
        WisieAnswerManager wisieAnswerManager = manager.getContainer().getWisieAnswerManager(profileId);
        if (container.getHints() <= 0 || !content.containsKey("answerId") || wisieAnswerManager == null) {
            return;
        }
        container.decreaseHints();
        Long markedAnswerId = ((Integer) content.get("answerId")).longValue();
        Boolean isAnswerCorrect = manager.getContainer().findCurrentCorrectAnswerId().equals(markedAnswerId);
        wisieAnswerManager.getFlow().hint(markedAnswerId, isAnswerCorrect);
    }
}
