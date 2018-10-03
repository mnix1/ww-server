package com.ww.manager.wisieanswer.state.skill.waterpistol;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.state.WisieState;
import com.ww.model.constant.wisie.WisieAnswerAction;
import io.reactivex.Flowable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static com.ww.helper.RandomHelper.randomDouble;

public class WisieStateCleaning extends WisieState {
    protected static final Logger logger = LoggerFactory.getLogger(WisieStateCleaning.class);

    public WisieStateCleaning(WisieAnswerManager manager) {
        super(manager, STATE_TYPE_FLOWABLE);
    }

    protected Flowable<Long> processFlowable() {
        manager.addAndSendAction(WisieAnswerAction.CLEANING);
        long interval = (long) (randomDouble(4 - manager.getSpeedF1() - manager.getReflexF1() - manager.getConcentrationF1() - manager.getConfidenceF1(),
                6 - 2 * manager.getSpeedF1() - 2 * manager.getReflexF1() - manager.getConcentrationF1() - manager.getConfidenceF1()) * 1000);
        logger.trace(manager.toString() + ", interval: " + interval);
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
