package com.ww.manager.heroanswer.state.phase3;

import com.ww.manager.heroanswer.HeroAnswerManager;
import com.ww.manager.heroanswer.state.State;
import com.ww.model.constant.hero.HeroAnswerAction;
import io.reactivex.Flowable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static com.ww.helper.RandomHelper.randomDouble;

public class StateStartLookingForAnswer extends State {
    protected static final Logger logger = LoggerFactory.getLogger(StateStartLookingForAnswer.class);

    public StateStartLookingForAnswer(HeroAnswerManager manager) {
        super(manager);
    }

    protected Flowable<Long> processFlowable() {
        manager.addAndSendAction(HeroAnswerAction.THINK_KNOW_ANSWER);
        long interval = (long) (randomDouble(1 - manager.getReflexF1(), 3 - 2 * manager.getReflexF1() - manager.getConcentrationF1()) * 1000);
        logger.debug(manager.getHero().getHero().getNamePolish() + ", " + manager.lastAction().name() + ", interval: " + interval);
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
