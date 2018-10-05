package com.ww.manager.wisieanswer.skill.state.kidnapping;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.state.WisieState;
import com.ww.model.constant.wisie.WisieAnswerAction;
import io.reactivex.Flowable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static com.ww.helper.RandomHelper.randomDouble;

public class WisieStateTryingToDefend extends WisieState {
    protected static final Logger logger = LoggerFactory.getLogger(WisieStateTryingToDefend.class);

    private long interval;

    public WisieStateTryingToDefend(WisieAnswerManager manager, long interval) {
        super(manager, STATE_TYPE_FLOWABLE);
        this.interval = interval;
    }

    protected Flowable<Long> processFlowable() {
        manager.addAndSendAction(WisieAnswerAction.TRYING_TO_DEFEND);
        logger.trace(describe() + ", interval: " + interval);
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
