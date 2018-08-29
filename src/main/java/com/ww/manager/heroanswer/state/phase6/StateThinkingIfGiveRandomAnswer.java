package com.ww.manager.heroanswer.state.phase6;

import com.ww.manager.heroanswer.HeroAnswerManager;
import com.ww.manager.heroanswer.state.State;
import com.ww.model.constant.hero.HeroAnswerAction;
import io.reactivex.Flowable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static com.ww.helper.RandomHelper.randomDouble;

public class StateThinkingIfGiveRandomAnswer extends State {
    protected static final Logger logger = LoggerFactory.getLogger(StateThinkingIfGiveRandomAnswer.class);
    private static final double MIN_INTERVAL = 0.4;

    public StateThinkingIfGiveRandomAnswer(HeroAnswerManager manager) {
        super(manager);
    }

    protected Flowable<Long> processFlowable() {
        manager.addAndSendAction(HeroAnswerAction.THINKING_IF_GIVE_RANDOM_ANSWER);
        //[0.4;2]max
        //[0.4;0.4]min
        double confidenceF1 = manager.f1(manager.getHero().getMentalAttributeConfidence());
        double speedF1 = manager.f1(manager.getHero().getMentalAttributeSpeed());
        double concentrationF1 = manager.f1(manager.getHero().getMentalAttributeConcentration());
        long interval = (long) (randomDouble(MIN_INTERVAL, (5 - 2 * confidenceF1 - concentrationF1 - speedF1) * MIN_INTERVAL) * 1000);
        logger.debug(manager.getHero().getHero().getNamePolish() + ", " + manager.lastAction().name() + ", confidenceF1: " + confidenceF1 + ", speedF1: " + speedF1 + ", concentrationF1: " + concentrationF1 + ", interval: " + interval);
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
