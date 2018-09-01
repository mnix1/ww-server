package com.ww.manager.wisieanswer.state.phase4;

import com.ww.manager.wisieanswer.WisieAnswerManager;
import com.ww.manager.wisieanswer.state.State;
import com.ww.model.constant.wisie.WisieAnswerAction;
import io.reactivex.Flowable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static com.ww.helper.RandomHelper.randomDouble;

public class StateEndRecognizingAnswers extends State {
    protected static final Logger logger = LoggerFactory.getLogger(StateEndRecognizingAnswers.class);

    public StateEndRecognizingAnswers(WisieAnswerManager manager) {
        super(manager);
    }

    protected Flowable<Long> processFlowable() {
        manager.addAndSendAction(WisieAnswerAction.RECOGNIZING_ANSWERS);
        double sumInterval = manager.getAnswerCount() * (3d - manager.getSpeedF1() - manager.getConcentrationF1()) * 500;
        if (manager.isHobby()) {
            sumInterval /= manager.getHobbyFactor();
        }
        long interval = (long) (sumInterval * (3 - manager.getSpeedF1() - manager.getConcentrationF1()));
        logger.debug(manager.getWisie().getWisie().getNamePolish() + ", " + manager.lastAction().name() + ", interval: " + interval);
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
