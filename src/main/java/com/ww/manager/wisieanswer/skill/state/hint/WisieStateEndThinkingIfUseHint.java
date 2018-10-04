package com.ww.manager.wisieanswer.skill.state.hint;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.state.WisieState;
import com.ww.model.constant.wisie.WisieAnswerAction;
import io.reactivex.Flowable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static com.ww.helper.RandomHelper.randomDouble;

public class WisieStateEndThinkingIfUseHint extends WisieState {
    protected static final Logger logger = LoggerFactory.getLogger(WisieStateEndThinkingIfUseHint.class);

    private boolean hintCorrect;

    public WisieStateEndThinkingIfUseHint(WisieAnswerManager manager, boolean hintCorrect) {
        super(manager, STATE_TYPE_FLOWABLE);
        this.hintCorrect = hintCorrect;
    }

    protected Flowable<Long> processFlowable() {
        manager.addAndSendAction(WisieAnswerAction.THINKING_IF_USE_HINT);
        double sumInterval = manager.getAnswerCount() * (4d - manager.getWisdomSum() - manager.getConfidenceF1() - manager.getIntuitionF1()) * intervalMultiply() / 10;
        if (hintCorrect) {
            sumInterval /= 2;
        }
        long interval = (long) (sumInterval * (4d - manager.getWisdomSum() - manager.getConfidenceF1() - manager.getIntuitionF1()));
        logger.trace(manager.toString() + ", interval: " + interval);
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
