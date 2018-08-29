package com.ww.manager.heroanswer.state.phase2;

import com.ww.manager.heroanswer.HeroAnswerManager;
import com.ww.manager.heroanswer.state.State;
import com.ww.model.constant.hero.HeroAnswerAction;
import io.reactivex.Flowable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static com.ww.helper.RandomHelper.randomDouble;

public class StateStartThinkingAboutQuestion extends State {
    protected static final Logger logger = LoggerFactory.getLogger(StateStartThinkingAboutQuestion.class);
    private static final double MIN_INTERVAL = 1;

    public StateStartThinkingAboutQuestion(HeroAnswerManager manager) {
        super(manager);
    }

    protected Flowable<Long> processFlowable() {
        manager.addAndSendAction(HeroAnswerAction.THINKING);
        //[1;10]max
        //[1;1]min
        double sumWisdomAttributeF1 = manager.sumWisdomAttributeF1();
        long interval = (long) (randomDouble(MIN_INTERVAL, (10 - 9 * sumWisdomAttributeF1) * MIN_INTERVAL) * 1000);
        logger.debug(manager.getHero().getHero().getNamePolish() + ", " + manager.lastAction().name() + ", sumWisdomAttributeF1: " + sumWisdomAttributeF1 + ", interval: " + interval);
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
