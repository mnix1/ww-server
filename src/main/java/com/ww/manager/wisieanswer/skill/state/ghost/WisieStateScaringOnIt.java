package com.ww.manager.wisieanswer.skill.state.ghost;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.state.WisieState;
import com.ww.model.constant.wisie.WisieAnswerAction;
import io.reactivex.Flowable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class WisieStateScaringOnIt extends WisieState {
    protected static final Logger logger = LoggerFactory.getLogger(WisieStateScaringOnIt.class);

    private long interval;

    public WisieStateScaringOnIt(WisieAnswerManager manager, long interval) {
        super(manager, STATE_TYPE_FLOWABLE);
        this.interval = interval;
    }

    protected Flowable<Long> processFlowable() {
        manager.addAndSendAction(WisieAnswerAction.SCARING_ON_IT);
        logger.trace(manager.toString() + ", interval: " + interval);
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
