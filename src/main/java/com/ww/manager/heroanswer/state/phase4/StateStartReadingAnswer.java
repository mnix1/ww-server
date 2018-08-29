package com.ww.manager.heroanswer.state.phase4;

import com.ww.manager.heroanswer.HeroAnswerManager;
import com.ww.manager.heroanswer.state.State;
import com.ww.model.constant.hero.HeroAnswerAction;
import io.reactivex.Flowable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static com.ww.helper.RandomHelper.randomDouble;

public class StateStartReadingAnswer extends State {
    protected static final Logger logger = LoggerFactory.getLogger(StateStartReadingAnswer.class);
    private static final double MIN_INTERVAL = 1;

    public StateStartReadingAnswer(HeroAnswerManager manager) {
        super(manager);
    }

    protected Flowable<Long> processFlowable() {
        manager.addAndSendAction(HeroAnswerAction.NOT_SURE_OF_ANSWER);
        //[1;5]max
        //[1;1]min
        double reflexF1 = manager.f1(manager.getHero().getMentalAttributeReflex());
        double speedF1 = manager.f1(manager.getHero().getMentalAttributeSpeed());
        long interval = (long) (randomDouble(MIN_INTERVAL, (5 - reflexF1 - 3 * speedF1) * MIN_INTERVAL) * 1000);
        logger.debug(manager.getHero().getHero().getNamePolish() + ", " + manager.lastAction().name() + ", reflexF1: " + reflexF1 + ", speedF1: " + speedF1 + ", interval: " + interval);
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
