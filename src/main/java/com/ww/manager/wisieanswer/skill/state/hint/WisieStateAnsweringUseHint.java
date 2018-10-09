package com.ww.manager.wisieanswer.skill.state.hint;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.skill.state.WisieSkillState;
import com.ww.model.constant.wisie.WisieAnswerAction;

public class WisieStateAnsweringUseHint extends WisieSkillState {
    private boolean hintCorrect;
    private Long hintAnswerId;

    public WisieStateAnsweringUseHint(WisieAnswerManager manager, boolean hintCorrect, Long hintAnswerId) {
        super(manager, STATE_TYPE_VOID);
        this.hintCorrect = hintCorrect;
        this.hintAnswerId = hintAnswerId;
    }

    @Override
    public String describe() {
        return super.describe() + ", hintCorrect=" + hintCorrect;
    }

    @Override
    protected void processVoid() {
        manager.addAndSendAction(WisieAnswerAction.ANSWERED);
        logger.trace(describe() + ", isHintCorrect: " + hintCorrect);
        manager.getWarManager().getFlow().wisieAnswered(manager.getOwnedWisie().getProfile().getId(), hintAnswerId);
    }
}
