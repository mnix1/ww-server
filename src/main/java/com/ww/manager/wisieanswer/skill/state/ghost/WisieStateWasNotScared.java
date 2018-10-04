package com.ww.manager.wisieanswer.skill.state.ghost;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.state.WisieState;
import com.ww.model.constant.wisie.WisieAnswerAction;
import io.reactivex.Flowable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static com.ww.helper.RandomHelper.randomDouble;

public class WisieStateWasNotScared extends WisieState {
    protected static final Logger logger = LoggerFactory.getLogger(WisieStateWasNotScared.class);

    public WisieStateWasNotScared(WisieAnswerManager manager) {
        super(manager, STATE_TYPE_FLOWABLE);
    }

    @Override
    protected Flowable<Long> processFlowable() {
        manager.addAndSendAction(WisieAnswerAction.WAS_NOT_SCARED);

        long interval = (long) (randomDouble(3 - manager.getSpeedF1() - manager.getReflexF1() - manager.getConcentrationF1(),
                4 - manager.getSpeedF1() - manager.getReflexF1() - 2 * manager.getConcentrationF1()) * intervalMultiply());
        logger.trace(manager.toString() + ", interval: " + interval);
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
