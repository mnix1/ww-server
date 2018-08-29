package com.ww.manager.heroanswer.state.phase1;

import com.ww.manager.heroanswer.HeroAnswerManager;
import com.ww.manager.heroanswer.state.State;
import com.ww.model.constant.hero.HeroAnswerAction;
import io.reactivex.Flowable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static com.ww.helper.RandomHelper.randomDouble;

public class StateStartReadingQuestion extends State {
    protected static final Logger logger = LoggerFactory.getLogger(StateStartReadingQuestion.class);
    private static final double MIN_INTERVAL = 0.5;

    public StateStartReadingQuestion(HeroAnswerManager manager) {
        super(manager);
    }

    protected Flowable<Long> processFlowable(){
        manager.addAndSendAction(HeroAnswerAction.WAITING_FOR_QUESTION);
        //[0.5;2.5]max
        //[0.5;0.5]min
        double reflexF1 = manager.f1(manager.getHero().getMentalAttributeReflex());
        long interval = (long) (randomDouble(MIN_INTERVAL, (5 - 4 * reflexF1) * MIN_INTERVAL) * 1000);
        logger.debug(manager.getHero().getHero().getNamePolish() + ", " + manager.lastAction().name() + ", reflexF1: " + reflexF1 +", interval: " + interval);
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
