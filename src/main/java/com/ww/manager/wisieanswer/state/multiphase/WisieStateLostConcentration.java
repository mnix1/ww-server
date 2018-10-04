package com.ww.manager.wisieanswer.state.multiphase;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.state.WisieState;
import com.ww.model.constant.wisie.WisieAnswerAction;
import io.reactivex.Flowable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static com.ww.helper.RandomHelper.randomDouble;
import static com.ww.helper.RandomHelper.randomElement;

public class WisieStateLostConcentration extends WisieState {
    protected static final Logger logger = LoggerFactory.getLogger(WisieStateLostConcentration.class);

    private WisieAnswerAction noConcentrationAction;

    public WisieStateLostConcentration(WisieAnswerManager manager, WisieAnswerAction noConcentrationAction) {
        super(manager, STATE_TYPE_FLOWABLE);
        this.noConcentrationAction = noConcentrationAction;
    }

    protected Flowable<Long> processFlowable() {
        manager.addAndSendAction(noConcentrationAction);
        long interval = (long) (randomDouble(2 - manager.getConcentrationF1(), 5 - manager.getConcentrationF1() * 4) * intervalMultiply());
        logger.trace(manager.toString() + ", interval: " + interval);
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
