package com.ww.manager.wisieanswer.skill.state.kidnapping;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.state.WisieState;
import com.ww.model.constant.wisie.WisieAnswerAction;
import io.reactivex.Flowable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class WisieStateKidnappingSucceeded extends WisieState {
    protected static final Logger logger = LoggerFactory.getLogger(WisieStateKidnappingSucceeded.class);

    private WisieAnswerManager opponent;

    public WisieStateKidnappingSucceeded(WisieAnswerManager manager, WisieAnswerManager opponent) {
        super(manager, STATE_TYPE_FLOWABLE);
        this.opponent = opponent;
    }

    @Override
    protected Flowable<Long> processFlowable() {
        manager.addAction(WisieAnswerAction.KIDNAPPING_SUCCEEDED);
        opponent.addAndSendAction(WisieAnswerAction.WAS_KIDNAPPED);
        long interval = intervalMultiply();
        logger.trace(manager.toString() + ", interval: " + interval);
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
