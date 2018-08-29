package com.ww.manager.heroanswer.state.phase2;

import com.ww.manager.heroanswer.HeroAnswerManager;
import com.ww.manager.heroanswer.state.State;
import com.ww.model.constant.hero.HeroAnswerAction;
import io.reactivex.Flowable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static com.ww.helper.RandomHelper.randomDouble;

public class StateEndReadingQuestion extends State {
    protected static final Logger logger = LoggerFactory.getLogger(StateEndReadingQuestion.class);
    private static final double MIN_INTERVAL = 2;

    public StateEndReadingQuestion(HeroAnswerManager manager) {
        super(manager);
    }

    protected Flowable<Long> processFlowable() {
        manager.addAndSendAction(HeroAnswerAction.READING_QUESTION);
        //[2;10]max
        //[2;2]min
        double speedF1 = manager.f1(manager.getHero().getMentalAttributeSpeed());
        long interval = (long) (randomDouble(MIN_INTERVAL, (5 - 4 * speedF1) * MIN_INTERVAL) * 1000);
        logger.debug(manager.getHero().getHero().getNamePolish() + ", " + manager.lastAction().name() + ", speedF1: " + speedF1 +", interval: " + interval);
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
