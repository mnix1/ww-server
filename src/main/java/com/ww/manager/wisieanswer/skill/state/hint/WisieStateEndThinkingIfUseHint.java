package com.ww.manager.wisieanswer.skill.state.hint;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.skill.state.WisieSkillState;
import com.ww.model.constant.wisie.WisieAnswerAction;
import io.reactivex.Flowable;

import java.util.concurrent.TimeUnit;

public class WisieStateEndThinkingIfUseHint extends WisieSkillState {
    private boolean hintCorrect;
    private Long interval;

    public WisieStateEndThinkingIfUseHint(WisieAnswerManager manager, boolean hintCorrect) {
        super(manager, STATE_TYPE_FLOWABLE);
        this.hintCorrect = hintCorrect;
    }

    @Override
    public String describe() {
        return super.describe() + ", interval=" + interval + ", hintCorrect=" + hintCorrect;
    }


    protected Flowable<Long> processFlowable() {
        manager.addAndSendAction(WisieAnswerAction.THINKING_IF_USE_HINT);
        double sumInterval = manager.getAnswerCount() * (4d - manager.getWarWisie().getWisdomSum() - manager.getWarWisie().getConfidenceF1() - manager.getWarWisie().getIntuitionF1()) * intervalMultiply() / 10;
        if (hintCorrect) {
            sumInterval /= 2;
        }
        interval = (long) (sumInterval * (4d - manager.getWarWisie().getWisdomSum() - manager.getWarWisie().getConfidenceF1() - manager.getWarWisie().getIntuitionF1()));
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
