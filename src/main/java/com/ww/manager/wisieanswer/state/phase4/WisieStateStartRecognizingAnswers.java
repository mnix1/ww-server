package com.ww.manager.wisieanswer.state.phase4;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.state.WisieState;
import com.ww.model.constant.wisie.WisieAnswerAction;
import io.reactivex.Flowable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static com.ww.helper.RandomHelper.randomDouble;

public class WisieStateStartRecognizingAnswers extends WisieState {
    protected static final Logger logger = LoggerFactory.getLogger(WisieStateStartRecognizingAnswers.class);

    public WisieStateStartRecognizingAnswers(WisieAnswerManager manager) {
        super(manager, STATE_TYPE_FLOWABLE);
    }

    protected Flowable<Long> processFlowable() {
        manager.addAndSendAction(WisieAnswerAction.NOT_SURE_OF_ANSWER);
        long interval = (long) (randomDouble(2 - manager.getReflexF1(), 4 - manager.getReflexF1() - manager.getConcentrationF1()) * 1000);
        logger.trace(manager.toString() + ", interval: " + interval);
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
