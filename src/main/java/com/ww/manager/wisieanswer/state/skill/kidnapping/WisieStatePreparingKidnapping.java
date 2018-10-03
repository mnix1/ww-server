package com.ww.manager.wisieanswer.state.skill.kidnapping;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.state.WisieState;
import com.ww.model.constant.wisie.WisieAnswerAction;
import io.reactivex.Flowable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static com.ww.helper.RandomHelper.randomDouble;

public class WisieStatePreparingKidnapping extends WisieState {
    protected static final Logger logger = LoggerFactory.getLogger(WisieStatePreparingKidnapping.class);

    public WisieStatePreparingKidnapping(WisieAnswerManager manager) {
        super(manager, STATE_TYPE_FLOWABLE);
    }

    protected Flowable<Long> processFlowable() {
        manager.addAndSendAction(WisieAnswerAction.PREPARING_KIDNAPPING);
        long interval = (long) (randomDouble(3 - manager.getSpeedF1() - manager.getReflexF1() - manager.getConfidenceF1(),
                6 - 2 * manager.getSpeedF1() - 2 * manager.getReflexF1() - 2 * manager.getConfidenceF1()) * 1000);
        logger.trace(manager.toString() + ", interval: " + interval);
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
