package com.ww.manager.heroanswer.state.multiphase;

import com.ww.manager.heroanswer.HeroAnswerManager;
import com.ww.manager.heroanswer.state.State;
import com.ww.model.constant.hero.HeroAnswerAction;
import io.reactivex.Flowable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

import static com.ww.helper.RandomHelper.randomDouble;
import static com.ww.helper.RandomHelper.randomElement;

public class StateLostConcentration extends State {
    protected static final Logger logger = LoggerFactory.getLogger(StateLostConcentration.class);
    private static final double MIN_INTERVAL = 1;

    private HeroAnswerAction noConcentrationAction;

    public StateLostConcentration(HeroAnswerManager manager, HeroAnswerAction noConcentrationAction) {
        super(manager);
        this.noConcentrationAction = noConcentrationAction;
    }

    protected Flowable<Long> processFlowable(){
        manager.addAndSendAction(noConcentrationAction);
        //[1;5]max
        //[1;1]min
        double concentrationF1 = manager.f1(manager.getHero().getMentalAttributeConcentration());
        long interval = (long) (randomDouble(MIN_INTERVAL, (5 - 4 * concentrationF1) * MIN_INTERVAL) * 1000);
        logger.debug(manager.getHero().getHero().getNamePolish() + ", " + manager.lastAction().name() + ", concentrationF1: " + concentrationF1 +", interval: " + interval);
        return Flowable.intervalRange(0L, 1L, interval, interval, TimeUnit.MILLISECONDS);
    }
}
