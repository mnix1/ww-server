package com.ww.manager.wisieanswer.state.phase3;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.state.State;
import com.ww.model.constant.wisie.WisieAnswerAction;
import io.reactivex.Flowable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static com.ww.helper.RandomHelper.randomDouble;

public class StateStartLookingForAnswer extends State {
    protected static final Logger logger = LoggerFactory.getLogger(StateStartLookingForAnswer.class);

    public StateStartLookingForAnswer(WisieAnswerManager manager) {
        super(manager, STATE_TYPE_FLOWABLE);
    }

    protected Flowable<Long> processFlowable() {
        manager.addAndSendAction(WisieAnswerAction.THINK_KNOW_ANSWER);
        long interval = (long) (randomDouble(1 - manager.getReflexF1(), 3 - 2 * manager.getReflexF1() - manager.getConcentrationF1()) * 1000);
        logger.trace(manager.toString() + ", interval: " + interval);
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
