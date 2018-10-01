package com.ww.manager.wisieanswer.state.phase1;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.state.State;
import com.ww.model.constant.wisie.WisieAnswerAction;
import io.reactivex.Flowable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static com.ww.helper.RandomHelper.randomDouble;

public class StateStartRecognizingQuestion extends State {
    protected static final Logger logger = LoggerFactory.getLogger(StateStartRecognizingQuestion.class);

    public StateStartRecognizingQuestion(WisieAnswerManager manager) {
        super(manager);
    }

    protected Flowable<Long> processFlowable() {
        manager.addAndSendAction(WisieAnswerAction.WAITING_FOR_QUESTION);
        long interval = (long) (randomDouble(1 - manager.getReflexF1(), 3 - 2 * manager.getReflexF1() - manager.getConcentrationF1()) * 1000);
        logger.trace(manager.toString() + ", interval: " + interval);
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
