package com.ww.manager.wisieanswer.state.phase3;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.state.State;
import com.ww.model.constant.wisie.WisieAnswerAction;
import io.reactivex.Flowable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static com.ww.helper.RandomHelper.randomDouble;

public class StateNoFoundAnswerLookingFor extends State {
    protected static final Logger logger = LoggerFactory.getLogger(StateNoFoundAnswerLookingFor.class);

    public StateNoFoundAnswerLookingFor(WisieAnswerManager manager) {
        super(manager);
    }

    protected Flowable<Long> processFlowable() {
        manager.addAndSendAction(WisieAnswerAction.NO_FOUND_ANSWER_LOOKING_FOR);
        long interval = (long) (randomDouble(2 - manager.getReflexF1() - manager.getConfidenceF1(), 4 - 2 * manager.getReflexF1() - 2 * manager.getConfidenceF1()) * 1000);
        logger.trace(manager.getWisie().getWisie().getNamePolish() + ", " + manager.lastAction().name() + ", interval: " + interval);
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
