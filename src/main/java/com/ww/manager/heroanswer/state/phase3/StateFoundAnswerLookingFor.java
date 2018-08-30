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

    public StateFoundAnswerLookingFor(HeroAnswerManager manager) {
        super(manager);
    }

    protected Flowable<Long> processFlowable() {
        manager.addAndSendAction(HeroAnswerAction.FOUND_ANSWER_LOOKING_FOR);
        long interval = (long) ((3 - manager.getReflexF1() - manager.getSpeedF1() - manager.getConcentrationF1()) * 1000);
        logger.debug(manager.getHero().getHero().getNamePolish() + ", " + manager.lastAction().name() + ", interval: " + interval);
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
