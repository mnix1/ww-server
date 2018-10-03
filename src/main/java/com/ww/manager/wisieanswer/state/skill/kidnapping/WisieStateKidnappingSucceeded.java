package com.ww.manager.wisieanswer.state.skill.kidnapping;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.state.WisieState;
import com.ww.model.constant.wisie.WisieAnswerAction;
import io.reactivex.Flowable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static com.ww.helper.RandomHelper.randomDouble;

public class WisieStateKidnappingSucceeded extends WisieState {
    protected static final Logger logger = LoggerFactory.getLogger(WisieStateKidnappingSucceeded.class);

    public WisieStateKidnappingSucceeded(WisieAnswerManager manager) {
        super(manager, STATE_TYPE_VOID);
    }

    @Override
    protected void processVoid() {
        manager.addAndSendAction(WisieAnswerAction.KIDNAPPING_SUCCEEDED);
        logger.trace(manager.toString());
    }
}
