package com.ww.manager.wisieanswer.state.hint;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.state.WisieState;
import com.ww.model.constant.wisie.WisieAnswerAction;
import io.reactivex.Flowable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static com.ww.helper.RandomHelper.randomDouble;

public class WisieStateDecidedIfUseHint extends WisieState {
    protected static final Logger logger = LoggerFactory.getLogger(WisieStateDecidedIfUseHint.class);
    private WisieAnswerAction wisieAnswerAction;

    public WisieStateDecidedIfUseHint(WisieAnswerManager manager, WisieAnswerAction wisieAnswerAction) {
        super(manager, STATE_TYPE_FLOWABLE);
        this.wisieAnswerAction = wisieAnswerAction;
    }

    protected Flowable<Long> processFlowable() {
        manager.addAndSendAction(wisieAnswerAction);
        long interval = (long) (randomDouble(1 - manager.getSpeedF1(), 2 - 2 * manager.getSpeedF1()) * 1000);
        logger.trace(manager.toString() + ", interval: " + interval);
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
