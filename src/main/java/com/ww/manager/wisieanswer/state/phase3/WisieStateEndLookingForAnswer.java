package com.ww.manager.wisieanswer.state.phase3;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.state.WisieState;
import com.ww.model.constant.wisie.WisieAnswerAction;
import io.reactivex.Flowable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static com.ww.helper.RandomHelper.randomDouble;

public class WisieStateEndLookingForAnswer extends WisieState {
    protected static final Logger logger = LoggerFactory.getLogger(WisieStateEndLookingForAnswer.class);

    public WisieStateEndLookingForAnswer(WisieAnswerManager manager) {
        super(manager, STATE_TYPE_FLOWABLE);
    }

    protected Flowable<Long> processFlowable() {
        manager.addAndSendAction(WisieAnswerAction.LOOKING_FOR_ANSWER);
        double sumInterval = manager.getAnswerCount() * (2d - manager.getSpeedF1()) * intervalMultiply() / 4;
        if (manager.isHobby()) {
            sumInterval /= manager.getHobbyFactor();
        }
        long interval = (long) (sumInterval * (4 - 2 * manager.getSpeedF1() - manager.getConcentrationF1()));
        logger.trace(describe() + ", interval: " + interval);
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
