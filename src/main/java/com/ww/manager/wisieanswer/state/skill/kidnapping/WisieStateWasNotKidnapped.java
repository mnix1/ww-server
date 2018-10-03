package com.ww.manager.wisieanswer.state.skill.kidnapping;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.state.WisieState;
import com.ww.model.constant.wisie.WisieAnswerAction;
import io.reactivex.Flowable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static com.ww.helper.RandomHelper.randomDouble;

public class WisieStateWasNotKidnapped extends WisieState {
    protected static final Logger logger = LoggerFactory.getLogger(WisieStateWasNotKidnapped.class);

    public WisieStateWasNotKidnapped(WisieAnswerManager manager) {
        super(manager, STATE_TYPE_FLOWABLE);
    }

    @Override
    protected Flowable<Long> processFlowable() {
        manager.addAndSendAction(WisieAnswerAction.KIDNAPPING_FAILED);
        long interval = (long) (randomDouble(1 - manager.getReflexF1(),
                2 - 1 * manager.getReflexF1() - 1 * manager.getConcentrationF1()) * 1000);
        logger.trace(manager.toString() + ", interval: " + interval);
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
