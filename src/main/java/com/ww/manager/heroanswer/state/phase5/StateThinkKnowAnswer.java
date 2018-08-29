package com.ww.manager.heroanswer.state.phase5;

import com.ww.manager.heroanswer.HeroAnswerManager;
import com.ww.manager.heroanswer.state.State;
import com.ww.model.constant.hero.HeroAnswerAction;
import io.reactivex.Flowable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static com.ww.helper.RandomHelper.randomDouble;

public class StateThinkKnowAnswer extends State {
    protected static final Logger logger = LoggerFactory.getLogger(StateThinkKnowAnswer.class);
    private static final double MIN_INTERVAL = 0.5;

    public StateThinkKnowAnswer(HeroAnswerManager manager) {
        super(manager);
    }

    protected Flowable<Long> processFlowable() {
        manager.addAndSendAction(HeroAnswerAction.THINK_KNOW_ANSWER);
        //[0.5;2.5]max
        //[0.5;0.5]min
        double intuitionF1 = manager.f1(manager.getHero().getMentalAttributeIntuition());
        double confidenceF1 = manager.f1(manager.getHero().getMentalAttributeConfidence());
        long interval = (long) (randomDouble(MIN_INTERVAL, (5 - 2 * intuitionF1 - 2 * confidenceF1) * MIN_INTERVAL) * 1000);
        logger.debug(manager.getHero().getHero().getNamePolish() + ", " + manager.lastAction().name() + ", intuitionF1: " + intuitionF1 + ", confidenceF1: " + confidenceF1 + ", interval: " + interval);
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
