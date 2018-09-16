package com.ww.manager.wisieanswer.state.phase6;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.state.State;
import com.ww.model.constant.wisie.WisieAnswerAction;
import io.reactivex.Flowable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static com.ww.helper.RandomHelper.randomDouble;

public class StateEndThinkingIfGiveRandomAnswer extends State {
    protected static final Logger logger = LoggerFactory.getLogger(StateEndThinkingIfGiveRandomAnswer.class);

    public StateEndThinkingIfGiveRandomAnswer(WisieAnswerManager manager) {
        super(manager);
    }

    protected Flowable<Long> processFlowable() {
        manager.addAndSendAction(WisieAnswerAction.THINKING_IF_GIVE_RANDOM_ANSWER);
        double sumInterval = randomDouble(1 - manager.getConfidenceF1(), 4 - 2 * manager.getConfidenceF1() - manager.getIntuitionF1());
        if (manager.isHobby()) {
            sumInterval /= manager.getHobbyFactor();
        }
        long interval = (long) (sumInterval * 1000);
        logger.trace(manager.getWisie().getWisie().getNamePolish() + ", " + manager.lastAction().name() + ", interval: " + interval);
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
