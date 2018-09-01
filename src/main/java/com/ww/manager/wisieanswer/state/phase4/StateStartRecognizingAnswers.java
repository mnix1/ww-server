package com.ww.manager.wisieanswer.state.phase4;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.state.State;
import com.ww.model.constant.wisie.WisieAnswerAction;
import io.reactivex.Flowable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static com.ww.helper.RandomHelper.randomDouble;

public class StateStartRecognizingAnswers extends State {
    protected static final Logger logger = LoggerFactory.getLogger(StateStartRecognizingAnswers.class);

    public StateStartRecognizingAnswers(WisieAnswerManager manager) {
        super(manager);
    }

    protected Flowable<Long> processFlowable() {
        manager.addAndSendAction(WisieAnswerAction.NOT_SURE_OF_ANSWER);
        long interval = (long) (randomDouble(2 - manager.getReflexF1(), 4 - manager.getReflexF1() - manager.getConcentrationF1()) * 1000);
        logger.debug(manager.getWisie().getWisie().getNamePolish() + ", " + manager.lastAction().name() + ", interval: " + interval);
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
