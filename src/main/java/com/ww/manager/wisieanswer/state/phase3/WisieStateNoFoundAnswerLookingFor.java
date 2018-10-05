package com.ww.manager.wisieanswer.state.phase3;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.state.WisieState;
import com.ww.model.constant.wisie.WisieAnswerAction;
import io.reactivex.Flowable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static com.ww.helper.RandomHelper.randomDouble;

public class WisieStateNoFoundAnswerLookingFor extends WisieState {
    protected static final Logger logger = LoggerFactory.getLogger(WisieStateNoFoundAnswerLookingFor.class);

    public WisieStateNoFoundAnswerLookingFor(WisieAnswerManager manager) {
        super(manager, STATE_TYPE_FLOWABLE);
    }

    protected Flowable<Long> processFlowable() {
        manager.addAndSendAction(WisieAnswerAction.NO_FOUND_ANSWER_LOOKING_FOR);
        long interval = (long) (randomDouble(2 - manager.getReflexF1() - manager.getConfidenceF1(), 4 - 2 * manager.getReflexF1() - 2 * manager.getConfidenceF1()) * intervalMultiply());
        logger.trace(describe() + ", interval: " + interval);
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
