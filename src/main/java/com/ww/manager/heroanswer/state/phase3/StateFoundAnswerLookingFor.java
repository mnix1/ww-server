package com.ww.manager.heroanswer.state.phase3;

import com.ww.manager.heroanswer.HeroAnswerManager;
import com.ww.manager.heroanswer.state.State;
import com.ww.model.constant.hero.HeroAnswerAction;
import io.reactivex.Flowable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static com.ww.helper.RandomHelper.randomDouble;

public class StateFoundAnswerLookingFor extends State {
    protected static final Logger logger = LoggerFactory.getLogger(StateFoundAnswerLookingFor.class);
    private static final double MIN_INTERVAL = 0.5;

    public StateFoundAnswerLookingFor(HeroAnswerManager manager) {
        super(manager);
    }

    protected Flowable<Long> processFlowable() {
        manager.addAndSendAction(HeroAnswerAction.FOUND_ANSWER_LOOKING_FOR);
        //[0.5;2.5]max
        //[0.5;0.5]min
        double reflexF1 = manager.f1(manager.getHero().getMentalAttributeReflex());
        double speedF1 = manager.f1(manager.getHero().getMentalAttributeSpeed());
        double concentrationF1 = manager.f1(manager.getHero().getMentalAttributeConcentration());
        long interval = (long) (randomDouble(MIN_INTERVAL, (5 - reflexF1 - 2 * speedF1 - concentrationF1) * MIN_INTERVAL) * 1000);
        logger.debug(manager.getHero().getHero().getNamePolish() + ", " + manager.lastAction().name() + ", reflexF1: " + reflexF1 + ", speedF1: " + speedF1 + ", concentrationF1: " + concentrationF1 + ", interval: " + interval);
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
