package com.ww.manager.heroanswer.state.phase3;

import com.ww.manager.heroanswer.HeroAnswerManager;
import com.ww.manager.heroanswer.state.State;
import com.ww.model.constant.hero.HeroAnswerAction;
import io.reactivex.Flowable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static com.ww.helper.RandomHelper.randomDouble;

public class StateEndLookingForAnswer extends State {
    protected static final Logger logger = LoggerFactory.getLogger(StateEndLookingForAnswer.class);
    private double getMinInterval() {
        return manager.getQuestion().getAnswers().size() * 0.2;
    }

    public StateEndLookingForAnswer(HeroAnswerManager manager) {
        super(manager);
    }

    protected Flowable<Long> processFlowable() {
        manager.addAndSendAction(HeroAnswerAction.LOOKING_FOR_ANSWER);
        double speedF1 = manager.f1(manager.getHero().getMentalAttributeSpeed());
        double concentrationF1 = manager.f1(manager.getHero().getMentalAttributeConcentration());
        double minInterval = getMinInterval();
        long interval = (long) (randomDouble(minInterval, (5 - 2 * speedF1 - 2 * concentrationF1) * minInterval) * 1000);
        logger.debug(manager.getHero().getHero().getNamePolish() + ", " + manager.lastAction().name() + ", speedF1: " + speedF1 +", concentrationF1: " + concentrationF1 +", interval: " + interval);
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
